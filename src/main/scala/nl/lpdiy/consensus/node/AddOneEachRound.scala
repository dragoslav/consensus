package nl.lpdiy.consensus.node

import nl.lpdiy.consensus.{ Message, Node, Environment }

class AddOneEachRound extends Node {

  private var round = 0
  private var messages: List[Message] = Nil

  override def initialize(environment: Environment, initialMessages: Set[Message]): Unit = messages = initialMessages.toList

  override def send(): Set[Message] = messages.take(round % messages.size).toSet

  override def receive(messages: Map[Int, Set[Message]]): Unit = round += 1
}
