package nl.lpdiy.consensus

import java.util.Random

import nl.lpdiy.consensus.agent._

object Main extends App {

  val numberOfRounds = 10

  val simulation = new Simulation(
    randomGenerator = new Random(0),
    numberOfAgents = 100,
    numberOfMessages = 500,
    agentConfig = Environment(
      connectionProbability = .1,
      erroneousAgentProbability = .30,
      initialMessageDistributionProbability = .05,
      numberOfRounds = numberOfRounds
    ),
    agentBuilder = new SimulationAgentBuilder(() ⇒ new CollectReduceCompliantAgent, () ⇒ new FlushAtFinish)
  )

  (0 until numberOfRounds).map(i ⇒ i → simulation.next()).foreach {
    r ⇒ println(f"#${r._1}%2d agents: ${r._2.numberOfAgents}%2d, messages: ${r._2.messages.size}%3d")
  }
}
