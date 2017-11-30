package nl.lpdiy.consensus

import java.util.Random

case class SimulationRoundResult(numberOfNodes: Int, messages: Set[Message])

class Simulation(
    randomGenerator:  Random,
    numberOfNodes:    Int,
    numberOfMessages: Int,
    nodeBuilder:      SimulationNodeBuilder,
    nodeConfig:       Environment
) {

  private val nodes = (0 until numberOfNodes).map {
    _ → (
      if (randomGenerator.nextDouble() > nodeConfig.erroneousNodeProbability) nodeBuilder.compliantNode() else nodeBuilder.erroneousNode()
    )
  }.toMap

  private val messages = (0 until numberOfMessages).map(_ ⇒ Message(randomGenerator.nextInt())).toSet

  nodes.values.foreach {
    _.initialize(
      nodeConfig,
      messages.filter(_ ⇒ randomGenerator.nextDouble() < nodeConfig.initialMessageDistributionProbability)
    )
  }

  def next(): SimulationRoundResult = {
    val nodeMessages = nodes.map { case (i, node) ⇒ i → node.send().intersect(messages) }

    nodes.foreach {
      case (i, node) ⇒ node.receive(
        nodes.filter {
          case (j, _) ⇒ i != j && randomGenerator.nextDouble() < nodeConfig.connectionProbability
        }.map {
          case (j, _) ⇒ j → nodeMessages(j)
        }
      )
    }

    consensusOf(nodes.values)
  }

  private def consensusOf(nodes: Iterable[Node]): SimulationRoundResult = {
    val counted = nodes.map(_.send().toList).filter(_.nonEmpty).groupBy(identity).toList.map {
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

class SimulationNodeBuilder(val compliantNode: () ⇒ Node, val erroneousNode: () ⇒ Node)