package playground

object MainApp extends App {
  println("Hello, World!!")

  def sum(a: Int, b: Int, c: Int): Int = a + b + c

  class PrinterPrinter {
    println("Hello, World2!!")
  }

  val filesHere = new java.io.File(".").listFiles

  val ss = for {
    file <- filesHere
    if file.getName.endsWith(".sh")
  } yield {
    println(file)
    file
  }

  ss foreach println

  val firstArg = if (args.length > 0) args(0) else ""

  val matchResult: Unit = firstArg match {
    case "salt" => println("pepper")
    case "chips" => println("salsa")
    case "eggs" => println("bacon")
    case _ => println("huh?")
  }

  val a = sum _
  val b = a.curried
  val c = b(3)
  val d = b _

  println(d()(3))
}
