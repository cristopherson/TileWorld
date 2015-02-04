package agents;

import behavior.AgreeTileRequest;
import behavior.ConfirmExistence;
import behavior.GameOver;
import behavior.InformExistence;
import behavior.InternalStateBehavior;
import behavior.RequestTile;
import behavior.TileAgentStates;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class TileAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalStateBehavior internalState;
	private InformExistence informExistence;
	private ConfirmExistence confirmExistence;
	private RequestTile requestTile;
	private AgreeTileRequest agreeTileRequest;
	private GameOver gameOver;
	
	public TileAgent() {
		internalState = new InternalStateBehavior();
		informExistence = new InformExistence();
		confirmExistence = new ConfirmExistence();
		requestTile = new RequestTile();
		agreeTileRequest = new AgreeTileRequest();
		gameOver = new GameOver();
	}

	public void setup() {
		System.out.println("Hello. My name is " + getLocalName() + "\n");
		
		internalState.registerFirstState(informExistence, TileAgentStates.INFORM_EXISTENCE);
		internalState.registerLastState(gameOver, TileAgentStates.GAME_OVER);
		internalState.registerState(confirmExistence, TileAgentStates.CONFIRM_EXISTENCE);
		internalState.registerState(requestTile, TileAgentStates.REQUEST_TILE);
		internalState.registerState(agreeTileRequest, TileAgentStates.AGREE_TILE_REQUEST);
		
		internalState.registerTransition(
				TileAgentStates.INFORM_EXISTENCE,
				TileAgentStates.CONFIRM_EXISTENCE,
				TileAgentStates.INFORM_EXISTENCE_EVENT);
		
		internalState.registerTransition(
				TileAgentStates.CONFIRM_EXISTENCE,
				TileAgentStates.REQUEST_TILE,
				TileAgentStates.CONFIRM_EXISTENCE_EVENT);
		
		internalState.registerTransition(
				TileAgentStates.REQUEST_TILE,
				TileAgentStates.AGREE_TILE_REQUEST,
				TileAgentStates.REQUEST_TILE_EVENT);
		
		internalState.registerTransition(
				TileAgentStates.AGREE_TILE_REQUEST,
				TileAgentStates.GAME_OVER,
				TileAgentStates.GAME_OVER_EVENT);
		
		addBehaviour(internalState);
	}
}
