package part2_event_sourcing

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import akka.actor.ActorSystem
import scala.sys.Prop
import akka.actor.Props
import java.{util => ju}

object PersistentActors extends App {
    
    // COMMANDS
    case class Invoice(recipient: String, date: ju.Date, amount: Int)
    case class InvoiceBulk(invoices: List[Invoice])

    // EVENTS
    case class InvoiceRecorded(id: Int, recipient: String, date: ju.Date, amount: Int)

    class Accountant extends PersistentActor with ActorLogging {

        var latestInvoiceId = 0
        var totalAmount = 0

        override def persistenceId: String = "simple-accountant"
        override def receiveCommand: PartialFunction[Any,Unit] = {
            case Invoice(recipient, date, amount) => 
                log.info(s"Recieve invoice for amount: $amount")

                val event = InvoiceRecorded(latestInvoiceId, recipient, date, amount)
                persist(event) { e => 
                    latestInvoiceId += 1
                    totalAmount += amount
                    log.info(s"Persisted $e as invoice #${e.id}, for total amount $totalAmount")
                }
            case InvoiceBulk(invoices) =>
                val invoiceIds = latestInvoiceId to (latestInvoiceId + invoices.length)
                val events = invoices.zip(invoiceIds).map { pair =>
                    val id = pair._2
                    val invoice = pair._1
                    InvoiceRecorded(id, invoice.recipient, invoice.date, invoice.amount)
                }
                persistAll(events) { e =>
                    latestInvoiceId += 1
                    totalAmount += e.amount
                    log.info(s"Persisted $e as invoice #${e.id}, for total amount $totalAmount")
                }
        }

        override def receiveRecover: PartialFunction[Any,Unit] = {
            case InvoiceRecorded(id, _, _, amount) => 
                latestInvoiceId = id
                totalAmount += amount
        }

        override def onPersistFailure(cause: Throwable, event: Any, seqNr: Long): Unit = {
            log.error(s"Fail to persist $event because of $cause")
            super.onPersistFailure(cause, event, seqNr)
        }

        override def onPersistRejected(cause: Throwable, event: Any, seqNr: Long): Unit = {
            log.error(s"Fail to persist $event because of $cause")
            super.onPersistRejected(cause, event, seqNr)
        }
    }

    val system = ActorSystem("PersistentActors")
    val accountant = system.actorOf(Props[Accountant], "simpleAccountant")

    for (i <- 1 to 10) {
        accountant ! Invoice("The Sofa Company", new ju.Date, i * 1000)
    }
}