package scalaswing

import scala.swing._

object ScalaSwing extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "Swing Application"
  }
  println("Hello, Swing!!!")
}
