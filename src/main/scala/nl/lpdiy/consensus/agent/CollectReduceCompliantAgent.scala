package nl.lpdiy.consensus.agent

import nl.lpdiy.consensus.{ Environment, Message, Agent }

class CollectReduceCompliantAgent extends Agent {

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = ???

  override def send(): Set[Message] = ???

  override def receive(messages: Map[Int, Set[Message]]): Unit = ???
}
