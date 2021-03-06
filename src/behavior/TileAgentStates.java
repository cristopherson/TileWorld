package behavior;

public interface TileAgentStates {
	public static final String INFORM_EXISTENCE = "InformExistence";
	public static final String CONFIRM_EXISTENCE = "ConfirmExistence";
	public static final String REQUEST_TILE = "RequestTile";
	public static final String AGREE_TILE_REQUEST = "AgreeTileRequest";
	public static final String PLAYING = "Playing";
	public static final String EXPLORING = "Exploring";
	public static final String PLANNING = "Planning";
	public static final String FILLING_HOLES = "FillingHoles";
	public static final String GAME_OVER = "GameOver";
	
	public static final int INFORM_EXISTENCE_EVENT = 0;
	public static final int CONFIRM_EXISTENCE_EVENT = 1;
	public static final int REQUEST_TILE_EVENT = 2;
	public static final int AGREE_TILE_REQUEST_EVENT = 3;
	public static final int PLAYING_EVENT = 4;
	public static final int EXPLORING_EVENT = 5;
	public static final int PLANNING_EVENT = 6;
	public static final int FILLING_HOLES_EVENT = 7;
	public static final int GAME_OVER_EVENT = 100;
}
