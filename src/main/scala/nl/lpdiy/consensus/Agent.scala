package nl.lpdiy.consensus

trait Agent {

  def initialize(environment: Environment, initialMessages: Set[Message])

  def send(): Set[Message]

  def receive(messages: Map[Int, Set[Message]]): Unit
}

case class Environment(
    connectionProbability:                 Double,
    erroneousAgentProbability:             Double,
    initialMessageDistributionProbability: Double,
    numberOfRounds:                        Int
) {
  assert(connectionProbability >= 0 && connectionProbability <= 1)
  assert(erroneousAgentProbability >= 0 && erroneousAgentProbability <= 1)
  assert(initialMessageDistributionProbability >= 0 && initialMessageDistributionProbability <= 1)
  assert(numberOfRounds > 0)
}
