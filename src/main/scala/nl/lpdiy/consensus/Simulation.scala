package nl.lpdiy.consensus

import java.util.Random

case class SimulationRoundResult(numberOfAgents: Int, messages: Set[Message])

class Simulation(
    randomGenerator:  Random,
    numberOfAgents:   Int,
    numberOfMessages: Int,
    agentBuilder:     SimulationAgentBuilder,
    agentConfig:      Environment
) {

  private val agents = (0 until numberOfAgents).map {
    _ → (
      if (randomGenerator.nextDouble() > agentConfig.erroneousAgentProbability) agentBuilder.compliantAgent() else agentBuilder.erroneousAgent()
    )
  }.toMap

  private val messages = (0 until numberOfMessages).map(_ ⇒ Message(randomGenerator.nextInt())).toSet

  agents.values.foreach {
    _.initialize(
      agentConfig,
      messages.filter(_ ⇒ randomGenerator.nextDouble() < agentConfig.initialMessageDistributionProbability)
    )
  }

  def next(): SimulationRoundResult = {
    val agentMessages = agents.map { case (i, agent) ⇒ i → agent.send().intersect(messages) }

    agents.foreach {
      case (i, agent) ⇒ agent.receive(
        agents.filter {
          case (j, _) ⇒ i != j && randomGenerator.nextDouble() < agentConfig.connectionProbability
        }.map {
          case (j, _) ⇒ j → agentMessages(j)
        }
      )
    }

    consensusOf(agents.values)
  }

  private def consensusOf(agents: Iterable[Agent]): SimulationRoundResult = {
    val counted = agents.map(_.send().toList).filter(_.nonEmpty).groupBy(identity).toList.map {
      case (k, l) ⇒ k → l.size
    }

    if (counted.isEmpty) SimulationRoundResult(0, Set())
    else {
      val max = counted.maxBy {
        case (_, s) ⇒ s
      }
      SimulationRoundResult(max._2, max._1.toSet)
    }
  }
}

class SimulationAgentBuilder(val compliantAgent: () ⇒ Agent, val erroneousAgent: () ⇒ Agent)