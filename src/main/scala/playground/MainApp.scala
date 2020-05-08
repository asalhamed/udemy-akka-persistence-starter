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

  var summation: Int = 0
  val someNumbers: List[Int] = List(-11, -10, -5, 0, 5, 10)
  val accSum = summation += (_: Int)

  someNumbers.foreach(accSum)
  println(summation)

  def filesMatching(matcher: String => Boolean) = {
    for (file <- filesHere; if matcher(file.getName))
      yield file
  }

  def filesEnding(query: String) = {
    filesMatching(_.endsWith(query))
  }

  def filesContaining(query: String) =
    filesMatching(_.contains(query))

  def filesRegex(query: String) =
    filesMatching(_.matches(query))

}
