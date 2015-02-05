package agents;

import behavior.AgreeTileRequest;
import behavior.ConfirmExistence;
import behavior.GameOver;
import behavior.InformExistence;
import behavior.InternalStateBehavior;
import behavior.LazyPlayingBehavior;
import behavior.PlayingBehavior;
import behavior.RandomPlayingBehavior;
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
	private PlayingBehavior playingBehavior;
	private GameOver gameOver;

	public TileAgent(PlayingBehavior behavior) {
		internalState = new InternalStateBehavior();
		informExistence = new InformExistence();
		confirmExistence = new ConfirmExistence();
		requestTile = new RequestTile();
		agreeTileRequest = new AgreeTileRequest();
		playingBehavior = behavior;
		gameOver = new GameOver();
	}

	public TileAgent() {
		this(new PlayingBehavior());
	}

	public void setup() {
		System.out.println("Hello. My name is " + getLocalName() + "\n");

		Object[] args = getArguments();
		if (args != null) {
			if(args[0].equals("random")) {
				System.out.println("I am a random agent");
				playingBehavior = new RandomPlayingBehavior();
			} else if(args[0].equals("lazy")){
				System.out.println("I am a lazy agent");
				playingBehavior = new LazyPlayingBehavior();
			}
		}

		internalState.registerFirstState(informExistence,
				TileAgentStates.INFORM_EXISTENCE);
		internalState.registerLastState(gameOver, TileAgentStates.GAME_OVER);
		internalState.registerState(confirmExistence,
				TileAgentStates.CONFIRM_EXISTENCE);
		internalState.registerState(requestTile, TileAgentStates.REQUEST_TILE);
		internalState.registerState(agreeTileRequest,
				TileAgentStates.AGREE_TILE_REQUEST);
		internalState.registerState(playingBehavior, TileAgentStates.PLAYING);

		internalState.registerTransition(TileAgentStates.INFORM_EXISTENCE,
				TileAgentStates.CONFIRM_EXISTENCE,
				TileAgentStates.INFORM_EXISTENCE_EVENT);

		internalState.registerTransition(TileAgentStates.CONFIRM_EXISTENCE,
				TileAgentStates.REQUEST_TILE,
				TileAgentStates.CONFIRM_EXISTENCE_EVENT);

		internalState.registerTransition(TileAgentStates.REQUEST_TILE,
				TileAgentStates.AGREE_TILE_REQUEST,
				TileAgentStates.REQUEST_TILE_EVENT);

		internalState.registerTransition(TileAgentStates.AGREE_TILE_REQUEST,
				TileAgentStates.PLAYING, TileAgentStates.PLAYING_EVENT);

		internalState.registerTransition(TileAgentStates.PLAYING,
				TileAgentStates.PLAYING, TileAgentStates.PLAYING_EVENT);

		internalState.registerTransition(TileAgentStates.PLAYING,
				TileAgentStates.GAME_OVER, TileAgentStates.GAME_OVER_EVENT);

		addBehaviour(internalState);
	}
}
