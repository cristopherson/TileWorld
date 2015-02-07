package behavior;

import java.util.Random;

import jade.lang.acl.ACLMessage;

public class LazyPlayingBehavior extends PlayingBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double laziness = 0.5; 

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgRx = myAgent.receive();
		if (msgRx == null) {
			block();
			return;
		} else if (msgRx.getPerformative() == ACLMessage.CFP) {
			if(new Random().nextDouble() < laziness) {
				state = PlayingStates.IDLE;
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("");
				msgTx.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(msgTx);
				System.out.println("Taking a rest");
				return;
			}
			
			if (state == PlayingStates.IDLE) {
				state = PlayingStates.EXPLORING;
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("random");
				msgTx.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(msgTx);
			} else if (state == PlayingStates.EXPLORING) {
				state = PlayingStates.PLANNING;
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("random");
				msgTx.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(msgTx);
			} else if (state == PlayingStates.PLANNING) {
				state = PlayingStates.MOVING;
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("random");
				msgTx.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(msgTx);
			} else if (state == PlayingStates.MOVING) {
				state = PlayingStates.IDLE;
				ACLMessage msgTx = msgRx.createReply();
				msgTx.setContent("random");
				msgTx.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(msgTx);
			}
		} else if (msgRx.getPerformative() == ACLMessage.FAILURE) {
			String content = msgRx.getContent();
			String action = content.split(":")[0];

			if (action.equals("moveto")) {
				int direction = Integer.parseInt(content.split(":")[1]);
				System.out.println("Direction (" + direction + ") failed");
				state = PlayingStates.IDLE;
			}
		} else if (msgRx.getPerformative() == ACLMessage.CONFIRM) {
			String content = msgRx.getContent();
			String action = content.split(":")[0];

			if (action.equals("moveto")) {
				int direction = Integer.parseInt(content.split(":")[1]);
				System.out.println("Direction (" + direction + ") succeded");
			}
		}
	}
}
