package agents;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Flower;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import behavior.AgentAction;

import utilities.TileGame;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Environment extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TileGame tileGame;
	private HashMap<String, Location> map;

	public Environment() {
		setMap(new HashMap<String, Location>());
		tileGame = new TileGame(this);
		tileGame.show();
	}

	public void setup() {
		addBehaviour(new CyclicBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msgRx = receive();
				if (msgRx != null) {
					if (msgRx.getPerformative() == ACLMessage.INFORM) {
						System.out.println(msgRx);
						ACLMessage msgTx = msgRx.createReply();
						msgTx.setPerformative(ACLMessage.CONFIRM);
						msgTx.setContent("Welcome "
								+ msgRx.getSender().getLocalName());
						send(msgTx);
					} else if (msgRx.getPerformative() == ACLMessage.REQUEST) {
						System.out.println(msgRx);
						String content = msgRx.getContent();

						if (content.split(":").length != 1) {
							String requestType = content.split(":")[1];
							
							if(requestType.equals("around")) {
								String agentName = msgRx.getSender().getLocalName();
								Location agentLocation = getMap().get(agentName);
								ArrayList<Location> occupiedLocations = tileGame.getGrid().getOccupiedAdjacentLocations(agentLocation);
								ArrayList<Location> availableLocations = tileGame.getGrid().getEmptyAdjacentLocations(agentLocation);							
								String message = "response:actors:";
								
								for(Location occupied : occupiedLocations) {
									Actor actor = tileGame.getGrid().get(occupied);
									if(actor.getClass() == Rock.class) {
										message += "rock,";
									} else if(actor.getClass() == Flower.class) {
										message += "flower,";
									} else if(actor.getClass() == Critter.class) {
										message += "critter,";
									} else {
										message += "unknown,";
									}
									
									message+=occupied.getRow()+ "," + occupied.getCol() + ":";
								}
								
								message +="empty:";
							
								for (Location location : availableLocations) {
									message+=location.getRow()+ "," + location.getCol() + ":";
								}
								message += "end";
								ACLMessage msgTx = msgRx.createReply();
								msgTx.setPerformative(ACLMessage.INFORM);
								msgTx.setContent(message);
								send(msgTx);								
							}
						} else {
							BugAgent bugAgent = new BugAgent(msgRx.getSender()
									.getLocalName().toString(),
									(Environment) myAgent);
							Location randomLocation = tileGame
									.getRandomEmptyLocation();
							tileGame.add(randomLocation, bugAgent);

							getMap().put(bugAgent.getAgentName(),
									randomLocation);

							ACLMessage msgTx = msgRx.createReply();
							msgTx.setPerformative(ACLMessage.AGREE);
							msgTx.setContent("Agent " + bugAgent.getAgentName()
									+ " has a place in this world");
							send(msgTx);
						}
					} else if (msgRx.getPerformative() == ACLMessage.PROPOSE) {
						String content = msgRx.getContent();
						String agentName = msgRx.getSender().getLocalName();
						Location location = map.get(agentName);

						if (content.equals("random")) {							
							if (location != null) {
								int action = ((int) (new Random().nextDouble() * (4)));
								if(action > 3) {
									action = AgentAction.MOVE_SOUTH;
								} else if(action > 2) {
									action = AgentAction.MOVE_NORTH;
								} else if(action > 1) {
									action = AgentAction.MOVE_RIGHT;
								} else {
									action = AgentAction.MOVE_LEFT;
								}
								executeAction(action, location);
							}
						} else if(content.contains("action")) {
							if (location != null) {
								String nextAction = content.split(":")[1];
								executeAction(Integer.parseInt(nextAction), location);
								
							}
						}
					}

				} else {
					block();
				}
			}
		});
	}

	public HashMap<String, Location> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Location> map) {
		this.map = map;
	}

	public void executeAction(int action, Location location) {
		Location adjacentLocation = null;
		BugAgent bugAgent = (BugAgent) tileGame.getGrid().get(location);
		boolean failure = true;

		System.out.println("Current location " + location);
		if (action == AgentAction.MOVE_LEFT) {
			action = Location.LEFT;
		} else if (action == AgentAction.MOVE_RIGHT) {
			action = Location.RIGHT;
		} else if (action == AgentAction.MOVE_NORTH) {
			action = Location.NORTH;
		} else if (action == AgentAction.MOVE_SOUTH) {
			action = Location.SOUTH;
		}

		adjacentLocation = location.getAdjacentLocation(action);

		if (adjacentLocation != null
				&& tileGame.getGrid().isValid(adjacentLocation)) {
			Actor actor = tileGame.getGrid().get(adjacentLocation);

			if (actor == null) {
				tileGame.remove(location);
				tileGame.add(adjacentLocation, bugAgent);
				map.put(bugAgent.getAgentName(), adjacentLocation);
				failure = false;
			} else if (actor.getClass() == Flower.class) {
				Location nextLocation = adjacentLocation
						.getAdjacentLocation(action);
				if (tileGame.getGrid().isValid(nextLocation)) {
					Actor nextActor = tileGame.getGrid().get(nextLocation);
					if (nextActor == null) {
						tileGame.remove(location);
						tileGame.remove(adjacentLocation);
						tileGame.add(adjacentLocation, bugAgent);
						tileGame.add(nextLocation, actor);
						map.put(bugAgent.getAgentName(), adjacentLocation);
						failure = false;
					} else if (nextActor.getClass() == Rock.class) {
						tileGame.remove(location);
						tileGame.remove(adjacentLocation);
						tileGame.remove(nextLocation);
						tileGame.add(adjacentLocation, bugAgent);
						map.put(bugAgent.getAgentName(), adjacentLocation);
						failure = false;
					}
				}
			}
		}

		if (failure) {
			ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
			msg.addReceiver(new AID(bugAgent.getAgentName(), AID.ISLOCALNAME));
			msg.setContent("moveto:" + action);
			send(msg);
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.addReceiver(new AID(bugAgent.getAgentName(), AID.ISLOCALNAME));
			msg.setContent("moveto:" + action);
			send(msg);
		}
	}
}
