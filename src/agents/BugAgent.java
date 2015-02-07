package agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import info.gridworld.actor.Bug;
import info.gridworld.grid.Location;

public class BugAgent extends Bug{
	private String agentName;
	private Environment env;
	
	public BugAgent(String agentName, Environment env) {
		super();
		this.setAgentName(agentName);
		this.setEnv(env);
		System.out.println("AgentName " + agentName);
	}
	
	public void act() {		
		Location location = env.getMap().get(agentName);		
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		
		msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));		
		msg.setContent("position:"+location.getCol()+":"+location.getRow());
		env.send(msg); 
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
}