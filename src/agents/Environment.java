package agents;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Location;

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
						BugAgent bugAgent = new BugAgent(msgRx.getSender()
								.getLocalName().toString(),
								(Environment) myAgent);
						Location randomLocation =tileGame.getRandomEmptyLocation(); 
						tileGame.add(randomLocation, bugAgent);
						
						getMap().put(bugAgent.getAgentName(), randomLocation);
												
						ACLMessage msgTx = msgRx.createReply();
						msgTx.setPerformative(ACLMessage.AGREE);
						msgTx.setContent("Agent " + bugAgent.getAgentName()
								+ " has a place in this world");
						send(msgTx);
					} else if (msgRx.getPerformative() == ACLMessage.PROPOSE) {
						String content = msgRx.getContent();
						String agentName = msgRx.getSender().getLocalName();

						if (content.equals("random")) {
							Location location = map.get(agentName);
							if (location != null) {
								int action = ((int) (new Random().nextDouble()
										* (AgentAction.MOVE_SOUTH + 1)));
								executeAction(action, location);
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
		BugAgent bugAgent = (BugAgent)tileGame.getGrid().get(location);
		
		System.out.println("Current location " +  location);
		if (action == AgentAction.MOVE_LEFT) {
			adjacentLocation = location.getAdjacentLocation(Location.LEFT);			
		} else if (action == AgentAction.MOVE_RIGHT) {
			adjacentLocation = location.getAdjacentLocation(Location.RIGHT);
		} else if (action == AgentAction.MOVE_NORTH) {
			adjacentLocation = location.getAdjacentLocation(Location.NORTH);
		} else if (action == AgentAction.MOVE_SOUTH) {
			adjacentLocation = location.getAdjacentLocation(Location.SOUTH);
		}
		
		if(adjacentLocation != null && isValid(adjacentLocation)) {
			tileGame.remove(location);			
			tileGame.add(adjacentLocation, bugAgent);
			map.put(bugAgent.getAgentName(), adjacentLocation);
			
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.addReceiver(new AID(bugAgent.getAgentName(), AID.ISLOCALNAME));
			msg.setContent("moveto:" + action);
			send(msg);			
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
			msg.addReceiver(new AID(bugAgent.getAgentName(), AID.ISLOCALNAME));
			msg.setContent("moveto:" + action);
			send(msg);			
		}
	}
	
	private boolean isValid(Location adjacentLocation) {
		boolean valid = false;
		
		valid = tileGame.getGrid().isValid(adjacentLocation);
				
		return valid;
	}
}
