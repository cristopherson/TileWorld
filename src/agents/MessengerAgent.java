package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class MessengerAgent extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setup() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("Peter", AID.ISLOCALNAME));
		msg.setLanguage("English");
		msg.setOntology("Weather-forecast-ontology");
		msg.setContent("Today it’s raining");
		send(msg);
	}

}
