package agents;

import utilities.TileGame;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Environment extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TileGame tileGame;;
	
	public Environment() {
		tileGame = new TileGame();
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
					System.out.println(msgRx);
					ACLMessage msgTx = msgRx.createReply();
					msgTx.setContent("Hello!");
					send(msgTx);
				} else {
					block();
				}
			}
		});
	}

}
