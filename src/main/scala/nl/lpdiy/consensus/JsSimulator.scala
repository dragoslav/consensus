package nl.lpdiy.consensus

import java.util.Random

import nl.lpdiy.consensus.agent._

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }
import scala.scalajs.js.{ Array ⇒ JsArray }

@JSExportTopLevel("JsSimulator")
class JsSimulator(
    seed:                                  Int,
    numberOfRounds:                        Int,
    numberOfAgents:                        Int,
    numberOfMessages:                      Int,
    connectionProbability:                 Double,
    erroneousAgentProbability:             Double,
    initialMessageDistributionProbability: Double,
    compliantAgent:                        Int,
    erroneousAgents:                       JsArray[Int]
) {

  private val buildCompliantAgent: () ⇒ Agent = () ⇒ compliantAgent match {
    case 0 ⇒ new CollectCompliantAgent
    case 1 ⇒ new CollectReduceCompliantAgent
    case _ ⇒ throw new UnsupportedOperationException
  }

  private val buildErroneousAgent: () ⇒ Agent = {
    val randomErroneousAgent = new Random(seed)

    val erroneousAgentBuilders = erroneousAgents.map {
      case 0 ⇒ () ⇒ new QuietAgent
      case 1 ⇒ () ⇒ new RepeatInitMessagesAgent
      case 2 ⇒ () ⇒ new AddOneEachRound
      case 3 ⇒ () ⇒ new FlushAtFinish
      case _ ⇒ throw new UnsupportedOperationException
    }

    () ⇒ erroneousAgentBuilders(randomErroneousAgent.nextInt(erroneousAgentBuilders.size))()
  }

  private val simulation = new Simulation(
    randomGenerator = new Random(seed),
    numberOfAgents = numberOfAgents,
    numberOfMessages = numberOfMessages,
    agentConfig = Environment(
      connectionProbability = connectionProbability,
      erroneousAgentProbability = erroneousAgentProbability,
      initialMessageDistributionProbability = initialMessageDistributionProbability,
      numberOfRounds = numberOfRounds
    ),
    agentBuilder = new SimulationAgentBuilder(buildCompliantAgent, buildErroneousAgent)
  )

  @JSExport
  def next(): JsArray[Int] = {
    val result = simulation.next()
    JsArray(result.numberOfAgents, result.messages.size)
  }
}
