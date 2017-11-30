package nl.lpdiy.consensus.node

import nl.lpdiy.consensus.{ Node, Environment, Message }

class CollectCompliantNode extends Node {

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = ???

  override def send(): Set[Message] = ???

  override def receive(messages: Map[Int, Set[Message]]): Unit = ???
}
