package util

object State extends Enumeration {
  type State = Value
  val GOING_HOME, SEARCHING, FOUND = Value
}
