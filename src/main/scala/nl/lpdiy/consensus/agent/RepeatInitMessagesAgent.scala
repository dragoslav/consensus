package nl.lpdiy.consensus.agent

import nl.lpdiy.consensus.{ Agent, Environment, Message }

import scala.collection.mutable

class RepeatInitMessagesAgent extends Agent {

  private val messages: mutable.Set[Message] = mutable.Set()

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = messages ++= initialMessages

  override def send(): Set[Message] = messages.toSet

  override def receive(messages: Map[Int, Set[Message]]): Unit = {}
}
