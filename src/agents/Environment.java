package agents;

import info.gridworld.actor.Bug;

import java.util.HashMap;
import java.util.LinkedList;

import utilities.TileGame;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Environment extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TileGame tileGame;
	private HashMap <String, BugAgent>map;
	
	public Environment() {
		setMap(new HashMap<String, BugAgent>());
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
						msgTx.setContent("Welcome " + msgRx.getSender().getLocalName());
						send(msgTx);
					} else if (msgRx.getPerformative() == ACLMessage.REQUEST) {
						System.out.println(msgRx);
						BugAgent bugAgent = new BugAgent(msgRx.getSender().getLocalName().toString(), (Environment)myAgent);
												
						getMap().put(bugAgent.getAgentName(), bugAgent);
						tileGame.add(tileGame.getRandomEmptyLocation(), bugAgent);
						
						ACLMessage msgTx = msgRx.createReply();
						msgTx.setPerformative(ACLMessage.AGREE);
						msgTx.setContent("Agent " + bugAgent.getAgentName() + " has a place in this world");
						send(msgTx);						
					}
					
				} else {
					block();
				}
			}
		});
	}

	public HashMap <String, BugAgent> getMap() {
		return map;
	}

	public void setMap(HashMap <String, BugAgent> map) {
		this.map = map;
	}
}
