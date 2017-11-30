package nl.lpdiy.consensus

import java.util.Random

import nl.lpdiy.consensus.node._

object Main extends App {

  val numberOfRounds = 10

  val simulation = new Simulation(
    randomGenerator = new Random(0),
    numberOfNodes = 100,
    numberOfMessages = 500,
    nodeConfig = Environment(
      connectionProbability = .1,
      erroneousNodeProbability = .30,
      initialMessageDistributionProbability = .05,
      numberOfRounds = numberOfRounds
    ),
    nodeBuilder = new SimulationNodeBuilder(() ⇒ new CollectReduceCompliantNode, () ⇒ new FlushAtFinish)
  )

  (0 until numberOfRounds).map(i ⇒ i → simulation.next()).foreach {
    r ⇒ println(f"#${r._1}%2d nodes: ${r._2.numberOfNodes}%2d, messages: ${r._2.messages.size}%3d")
  }
}

