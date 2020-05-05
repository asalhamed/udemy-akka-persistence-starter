import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import scala.collection.mutable

object PersistentActorsExercise extends App {
    
    // Commands
    case class Vote(citizenPID: String, candidate: String)
    
    // Events
    trait VoteEvent
    case class VoteAccepted(citizenPID: String, candidate: String)
    case class VoteRejected(citizenPID: String, candidate: String)

    // Actor
    class VoteActor extends PersistentActor with ActorLogging {

        var votesMap: mutable.HashMap[String, Vote] = mutable.HashMap()
        var candidates: mutable.HashMap[String, mutable.Set[Vote]] = mutable.HashMap()

        override def persistenceId: String = "vote-machine"
        override def receiveRecover: PartialFunction[Any,Unit] = {
            case VoteAccepted(citizenPID, candidate) =>
                val vote = Vote(citizenPID, candidate)
                if(!candidates.contains(candidate))
                    candidates += ((candidate, mutable.Set.empty))
                votesMap += ((citizenPID, Vote(citizenPID, candidate)))
                candidates(candidate) += Vote(citizenPID, candidate)
            case VoteRejected(citizenPID, candidate) => 
                log.info(s"Ignoring Vote rejected for $candidate by $citizenPID")
        }

        override def receiveCommand: PartialFunction[Any,Unit] = {
            case Vote(citizenPID,candidate) => 
                val event = if(votesMap.contains(citizenPID))
                        VoteRejected(citizenPID, candidate)
                    else
                        VoteAccepted(citizenPID, candidate)
                persist(event) { e => e match {
                        case VoteAccepted(citizenPID, candidate) =>
                            val vote = Vote(citizenPID, candidate)
                            if(!candidates.contains(candidate))
                                candidates += ((candidate, mutable.Set.empty))
                            votesMap += ((citizenPID, Vote(citizenPID, candidate)))
                            candidates(candidate) += Vote(citizenPID, candidate)
                        case VoteRejected(citizenPID, candidate) => 
                            log.info(s"Vote rejected for $candidate by $citizenPID")
                    }
                }
        }
        
    }
}
