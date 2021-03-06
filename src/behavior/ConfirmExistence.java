package behavior;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class ConfirmExistence extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isDone = false;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgRx = myAgent.receive();
		if (msgRx != null) {
			if (msgRx.getPerformative() == ACLMessage.CONFIRM ) {
				System.out.println(msgRx);
				isDone = true;
			} 
		} else {
			block();
		}		
		
	}

	public int onEnd() {
		return TileAgentStates.CONFIRM_EXISTENCE_EVENT;		
	}
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return isDone;
	}

}
