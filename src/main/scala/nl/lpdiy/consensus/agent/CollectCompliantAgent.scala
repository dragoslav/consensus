package nl.lpdiy.consensus.agent

import nl.lpdiy.consensus.{ Agent, Environment, Message }

class CollectCompliantAgent extends Agent {

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = ???

  override def send(): Set[Message] = ???

  override def receive(messages: Map[Int, Set[Message]]): Unit = ???
}
