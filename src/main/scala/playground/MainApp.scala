package playground

object MainApp extends App {
  println("Hello, World!!")

  class PrinterPrinter {
    println("Hello, World2!!")
  }
  new PrinterPrinter

  println(2 / new Rational(4))

  val filesHere = new java.io.File(".").listFiles

  val ss = for {
    file <- filesHere
    if file.getName.endsWith(".sh")
  } yield file
  ss foreach println
}
