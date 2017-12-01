## Consensus from Trust

### Problem

Distributed agents can communicate between each other. 
Each agent has its own initial set of messages (e.g. sensory input) and goal is to communicate with other agents and reach the consensus in limited amount of time: 
- for sake of simplicity communication is done by repeating limited number of send/receive rounds (iterations)
- each agent sends (broadcasts) messages in each round to random subset of other agents, e.g. to randomly selected 10% of them
- due to communication problems it is possible to receive an empty or incomplete set of messages
- some agents may not work properly or even be malicious - [Byzantine generals problem](https://en.wikipedia.org/wiki/Byzantine_fault_tolerance#Byzantine_Generals.27_Problem)
- every received message is **valid** regardless of senders intention
- after the last round agents send what they **believe** is set of messages majority will agree upon (consensus) - thus the most of agents should agree upon exactly the same set of messages
- alternative view of consensus - after the communication is over, each agent will perform specific action based on its believe. Each agent should have as many messages as possible (truth) and goal is to have the maximal number of agents acting in coordinated way ([swarm intelligence](https://en.wikipedia.org/wiki/Swarm_intelligence)). 

This project is inspired by [Bitcoin and Cryptocurrency Technologies Coursera course](https://www.coursera.org/learn/cryptocurrency) (assignment 2) and it is also covered [here](http://bitcoinbook.cs.princeton.edu/).

### Algorithm

Let's imagine first that there are no malicious agents and there is no problem in communication:
- agents broadcast initial set of messages
- agents collect all messages they receive and in the following rounds send them all
- consensus should be reached if there is enough number of rounds as in this [example](https://dragoslav.github.io/consensus/?seed=0&rounds=10&nn=100&nm=500&cp=0.05&ep=0.3&mp=0.05&cn=0&en=)

Now let's introduce agents that may broadcast **new** messages each round - [example](https://dragoslav.github.io/consensus/?seed=0&rounds=10&nn=100&nm=500&cp=0.05&ep=0.3&mp=0.05&cn=0&en=2,3).
Effectively probability of optimal consensus is lowered because not all messages can be propagated to all agents.

Let's extend the algorithm: the first phase is already described (collecting all messages) and let's assume that this phase lasts for the first half of all rounds (first half of the time) and the following second phase lasts until the end (reduction phase).
After an agent receives the set of messages from other agents:
- the agent must decide to which agents in **this and only this** round it can trust (without blacklisting suspicious agents forever)
- other agent is trustworthy only if set of messages sent by it is **similar** enough to set that this agent already has - e.g. size difference less than 10% and intersection contains at least 90% of already collected messages
- the agent must remove any message (from collected set) that is not present in majority of sets of other **trustworthy** agents.

Adjusting the previous simulation to use *Collect/Reduce* compliant agent, result is [this](https://dragoslav.github.io/consensus/?seed=0&rounds=10&nn=100&nm=500&cp=0.05&ep=0.3&mp=0.05&cn=1&en=2,3).

### Additional Notes

This repo **does not** contain the source code for *Collect All* and *Collect/Reduce* compliant agents because that would allow participants of the Coursera course to pass the second assignment (81% and 100% scores respectively).
However in the course forum there are already many descriptions how to implement different solutions. Some solutions are in a way cheating: using static members or having a "secret" protocol for transmitting messages between compliant agents and thus allowing them to recognize each other and ignore erroneous/malicious agents.
IMHO described solutions that achieve 100% score are too complicated - using for instance complex agent blacklisting - not to mention that the most of other complex solutions don't even can score 100%.

On the other hand this repo contains code for the [simulator](https://dragoslav.github.io/consensus/), erroneous agents (some examples) and optionally to run simulations from the command line.

#### Blacklisting Agents

In the original course assignment it is assumed that agents (nodes) can be either compliant or malicious.
The common approach would be (according to the forum) to detect malicious agents and blacklist them forever (+ ignore all past/future messages from them etc.).
Complexity comes from the way how to detect them, e.g. based on some probability, keeping different internal states until the end and decide perhaps only then etc.
Here it is proposed that agents may be ignored only in a single round - effectively blacklisted for the current round only.
It may be counter-intuitive but blacklisting agents forever actually can lead to specific attack - malicious agents can cause some compliant agents to appear malicious to other compliant agents and if in that case malicious become majority (>50%) then consensus among compliant agents may not be reached at all.
For instance hypothetically if just before the end of phase 1 malicious agents send many new messages to compliant agents, those agent may appear malicious in the first round of phase 2 (message sets are different enough) and then if suddenly is more than 50% of malicious agents, they can "drain" messages from compliant agents.
Just by not blacklisting forever it is possible to solve broader problem where communication errors are also possible between otherwise compliant agents.

#### Improvements

Agent environment/protocol variables:
- connection probability - to how many agents (%) connection is established in each round - effectively probability of an edge if agents are modeled as nodes in a graph
- erroneous agent probability - probability that sender agent is erroneous (effectively communication error) or malicious
- initial message distribution
- number of rounds

Based on these variables and assuming that they are know in advance, compliant agents should have following parameters set: 
- round threshold - when to start phase 2
- message threshold - probability that message is kept in phase 2
- trustworthiness threshold - probability that other agent is trustworthy

Based on environment variables better outcome can be achieved by tweaking parameters, however in the simulator these parameters are static: 50%, 95% and 90% respectively.

Other improvement can be to allow adding messages in phase 2 if they are sent by most of trustworthy agents - but in certain environment this may even lead to sub-optimal consensus if threshold parameters are properly adjusted.
  
#### Building & Running

- install Java 8 or 9, Scala 2.12.x and `sbt`
- run `./build.sh`, check out `./dist` directory for html/js simulator
- check `nl.lpdiy.consensus.Main` for standalone simulation executions
