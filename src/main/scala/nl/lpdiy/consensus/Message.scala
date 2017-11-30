package nl.lpdiy.consensus

case class Message(id: Int) {

  override def hashCode(): Int = id

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: Message ⇒ id == that.id
    case _             ⇒ false
  }
}
