package behavior;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestTile extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgTx = new ACLMessage(ACLMessage.REQUEST);
		msgTx.setContent("I want a position in the world\n");
		msgTx.addReceiver(new AID("Env", AID.ISLOCALNAME));
		myAgent.send(msgTx);
	}
	
	public int onEnd() {
		return TileAgentStates.REQUEST_TILE_EVENT;		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
