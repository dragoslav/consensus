package nl.lpdiy.consensus.node

import nl.lpdiy.consensus.{ Message, Environment }

class RepeatInitMessagesNode extends CollectCompliantNode {

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = super.initialize(environment, filter(initialMessages))

  override def receive(messages: Map[Int, Set[Message]]): Unit = messages.map {
    case (id, set) ⇒ id → filter(set)
  }

  private def filter: Set[Message] ⇒ Set[Message] = _.filter(_.id % 2 == 0)
}
