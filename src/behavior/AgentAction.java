package behavior;

import info.gridworld.grid.Location;

public interface AgentAction {
	public static int MOVE_LEFT = Location.LEFT;
	public static int MOVE_RIGHT = Location.RIGHT;
	public static int MOVE_NORTH = Location.NORTH;
	public static int MOVE_SOUTH = Location.SOUTH;
}