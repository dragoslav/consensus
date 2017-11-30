package nl.lpdiy.consensus

import java.util.Random

import nl.lpdiy.consensus.node._

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }
import scala.scalajs.js.{ Array ⇒ JsArray }

@JSExportTopLevel("JsSimulator")
class JsSimulator(
    seed:                                  Int,
    numberOfRounds:                        Int,
    numberOfNodes:                         Int,
    numberOfMessages:                      Int,
    connectionProbability:                 Double,
    erroneousNodeProbability:              Double,
    initialMessageDistributionProbability: Double,
    compliantNode:                         Int,
    erroneousNodes:                        JsArray[Int]
) {

  private val buildCompliantNode: () ⇒ Node = () ⇒ compliantNode match {
    case 0 ⇒ new CollectCompliantNode
    case 1 ⇒ new CollectReduceCompliantNode
    case _ ⇒ throw new UnsupportedOperationException
  }

  private val buildErroneousNode: () ⇒ Node = {
    val randomErroneousNode = new Random(seed)

    val erroneousNodeBuilders = erroneousNodes.map {
      case 0 ⇒ () ⇒ new QuietNode
      case 1 ⇒ () ⇒ new RepeatInitMessagesNode
      case 2 ⇒ () ⇒ new AddOneEachRound
      case 3 ⇒ () ⇒ new FlushAtFinish
      case _ ⇒ throw new UnsupportedOperationException
    }

    () ⇒ erroneousNodeBuilders(randomErroneousNode.nextInt(erroneousNodeBuilders.size))()
  }

  private val simulation = new Simulation(
    randomGenerator = new Random(seed),
    numberOfNodes = numberOfNodes,
    numberOfMessages = numberOfMessages,
    nodeConfig = Environment(
      connectionProbability = connectionProbability,
      erroneousNodeProbability = erroneousNodeProbability,
      initialMessageDistributionProbability = initialMessageDistributionProbability,
      numberOfRounds = numberOfRounds
    ),
    nodeBuilder = new SimulationNodeBuilder(buildCompliantNode, buildErroneousNode)
  )

  @JSExport
  def next(): JsArray[Int] = {
    val result = simulation.next()
    JsArray(result.numberOfNodes, result.messages.size)
  }
}
