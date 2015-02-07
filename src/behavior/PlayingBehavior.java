package behavior;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class PlayingBehavior extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean gameOver = false;
	protected PlayingStates state;

	public PlayingBehavior() {
		state = PlayingStates.IDLE;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgRx = myAgent.receive();
		if (msgRx == null) {
			block();
			return;
		} else if (msgRx.getPerformative() == ACLMessage.CFP) {
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

	public int onEnd() {
		if (!gameOver)
			return TileAgentStates.PLAYING_EVENT;
		return TileAgentStates.GAME_OVER_EVENT;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return gameOver;
	}
}
