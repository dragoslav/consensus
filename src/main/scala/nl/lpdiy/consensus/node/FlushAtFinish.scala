package nl.lpdiy.consensus.node

import nl.lpdiy.consensus.{ Environment, Message, Node }

import scala.collection.mutable

class FlushAtFinish extends Node {

  private var round = 0
  private var environment: Environment = _

  private val messages: mutable.Set[Message] = mutable.Set()

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = {
    messages ++= initialMessages
    this.environment = environment
  }

  override def send(): Set[Message] = if (round == environment.numberOfRounds - 2) messages.toSet else Set()

  override def receive(messages: Map[Int, Set[Message]]): Unit = round += 1
}
