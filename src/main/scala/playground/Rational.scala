package playground

object Rational {
  implicit def intToRational(x: Int): Rational = new Rational(x)
}

class Rational(n: Int, d: Int) {

  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  val numer: Int = n / g
  val denom: Int = d / g

  def this(n: Int) = this(n, 1)

  def +(that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom
    )

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)

  override def toString = s"$numer/$denom"

  @scala.annotation.tailrec
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  println(2 * new Rational(2,1))
}
