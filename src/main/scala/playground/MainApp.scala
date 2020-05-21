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


  val firstArg = if (args.length > 0) args(0) else ""

  val matchResult: Unit = firstArg match {
    case "salt"  => println("pepper")
    case "chips" => println("salsa")
    case "eggs"  => println("bacon")
    case _       => println("huh?")
  }

  val a = sum _
  val b = a.curried
  val c = b(3)
  val d = b _

  val tp3 = b(3)(2)

  def addThen(x: Int, y: Int)(doThis: Int => Unit) = {
    doThis(x+y)
  }

  addThen(8, 9) {
    println(_)
  }

  println(d()(3))

  class Money(val amount: Double) extends AnyVal

  var summation: Int = 0
  val someNumbers: List[Int] = List(-11, -10, -5, 0, 5, 10)
  val accSum: Int => Unit = summation += (_: Int)

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

  abstract class Element {
    def contents: Array[String]
    def height: Int = contents.length
    def width: Int =
      if (height == 0) 0 else contents(0).length
  }

  class ArrayElement(override val contents: Array[String]) extends Element

  class LineElement(s: String) extends ArrayElement(Array(s)) {
    override def width: Int = s.length
    override def height: Int = 1
  }

  val ele = new ArrayElement(Array("Hello"))

  ele.contents foreach { x =>
    println(x)
    println(x)
  }

  class UniformElement(
      ch: Char,
      override val width: Int,
      override val height: Int
  ) extends Element {
    private val line = ch.toString * width
    def contents: Array[String] = {
      Array.fill(height) {
        line
      }
    }
  }

  val e1: Element = new ArrayElement(Array("hello", "world"))
  val ae: ArrayElement = new LineElement("hello")
  val e2: Element = ae
  val e3: Element = new UniformElement('x', 2, 3)

  abstract class Cat[-T, +U] {
    def meow[W](volume: T, listener: Cat[U, T])
      : Cat[Cat[U, T], U]
  }
}
