package utilities;

import agents.Environment;
import info.gridworld.actor.Bug;

public class BugAgent extends Bug{
	private String agentName;
	private Environment env;
	
	public BugAgent(String agentName, Environment env) {
		this.setAgentName(agentName);
		this.setEnv(env);
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
