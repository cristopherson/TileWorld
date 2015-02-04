package behavior;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class InformExistence extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgTx = new ACLMessage(ACLMessage.INFORM);
		msgTx.setContent("Hello. My name is " + myAgent.getLocalName() + "\n");
		msgTx.addReceiver(new AID("Env", AID.ISLOCALNAME));
		myAgent.send(msgTx);		
	}

	public int onEnd() {
		return TileAgentStates.INFORM_EXISTENCE_EVENT;		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
