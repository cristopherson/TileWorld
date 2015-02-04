package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class TileAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setup() {
		System.out.println("Hello. My name is " + getLocalName() + "\n");
		addBehaviour(new OneShotBehaviour() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msgTx = new ACLMessage(ACLMessage.PROPOSE);
				msgTx.setContent("Hello!");
				msgTx.addReceiver(new AID("Env", AID.ISLOCALNAME));
				send(msgTx);
			}
		});
	}
}
