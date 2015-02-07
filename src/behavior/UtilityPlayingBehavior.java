package behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Flower;
import info.gridworld.actor.Rock;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import jade.lang.acl.ACLMessage;

public class UtilityPlayingBehavior extends PlayingBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String environmentState = "";
	String action = "";
	Location currentLocation;
	Grid<Actor> grid = new BoundedGrid<Actor>(10, 10);
	Location rockLocation = null;
	Location flowerLocation = null;
	Location centrLocation = new Location(5, 5);
	String goal = "center";
	int failures = 0;
	boolean randomize = false;
	LinkedList<String> nextActionsList = new LinkedList<String>();
	private HashMap<Integer, Double> probabiltyMap;

	public UtilityPlayingBehavior() {
		super();
		probabiltyMap = new HashMap<Integer, Double>();

		probabiltyMap.put(AgentAction.MOVE_LEFT, 0.25);
		probabiltyMap.put(AgentAction.MOVE_RIGHT, 0.25);
		probabiltyMap.put(AgentAction.MOVE_NORTH, 0.25);
		probabiltyMap.put(AgentAction.MOVE_SOUTH, 0.25);
	}

	public String calculateUtility() {
		ArrayList<Location> locations = grid
				.getValidAdjacentLocations(currentLocation);
		double goalUtility = 1000;
		Location nextAction = null;
		String nextActionMessage = "";

		for (Location location : locations) {
			Actor actor = grid.get(location);
			int nextCol = location.getCol();
			int nextRow = location.getRow();
			int goalCol;
			int goalRow;

			System.out.println("Checking location " + location);
			if (actor != null) {
				if (actor.getClass() == Rock.class) {
					rockLocation = location;
				} else if (actor.getClass() == Flower.class) {
					flowerLocation = location;
				}
			}

			if (flowerLocation != null && rockLocation != null) {
				goal = "fillholes";
			} else {
				goal = "center";
			}

			if (goal.equals("center")) {
				double currentGoalUtility;
				goalCol = centrLocation.getCol();
				goalRow = centrLocation.getRow();

				currentGoalUtility = Math.abs((goalRow - nextRow)
						+ (goalCol - nextCol));

				if (nextCol == goalCol) {
					if (nextRow > goalRow) {
						currentGoalUtility /= probabiltyMap
								.get(AgentAction.MOVE_SOUTH);
					} else {
						currentGoalUtility /= probabiltyMap
								.get(AgentAction.MOVE_NORTH);
					}
				} else if (nextCol > goalCol) {
					currentGoalUtility /= probabiltyMap
							.get(AgentAction.MOVE_RIGHT);
				} else {
					currentGoalUtility /= probabiltyMap
							.get(AgentAction.MOVE_LEFT);
				}

				if (currentGoalUtility < goalUtility) {
					goalUtility = currentGoalUtility;
					nextAction = location;
				}
			} else {
				double currentGoalUtility;
				goalCol = flowerLocation.getCol();
				goalRow = flowerLocation.getRow();

				currentGoalUtility = Math.abs((goalRow - nextRow)
						+ (goalCol - nextCol));

				if (nextCol == goalCol) {
					if (nextRow > goalRow) {
						currentGoalUtility /= probabiltyMap
								.get(AgentAction.MOVE_SOUTH);
					} else {
						currentGoalUtility /= probabiltyMap
								.get(AgentAction.MOVE_NORTH);
					}
				} else if (nextCol > goalCol) {
					currentGoalUtility /= probabiltyMap
							.get(AgentAction.MOVE_RIGHT);
				} else {
					currentGoalUtility /= probabiltyMap
							.get(AgentAction.MOVE_LEFT);
				}

				if (Math.abs(currentGoalUtility) < goalUtility) {
					goalUtility = currentGoalUtility;
					nextAction = location;
				}
			}
		}

		if (nextAction != null) {
			nextActionMessage += "action:";
			System.out.println("My next locations is " + nextAction);
			if (nextAction.getCol() == currentLocation.getCol()) {
				if (nextAction.getRow() > currentLocation.getRow()) {
					nextActionMessage += AgentAction.MOVE_SOUTH;
				} else {
					nextActionMessage += AgentAction.MOVE_NORTH;
				}
			} else if (nextAction.getCol() > currentLocation.getCol()) {
				nextActionMessage += AgentAction.MOVE_RIGHT + ",";
				if (nextAction.getRow() > currentLocation.getRow()) {
					nextActionMessage += AgentAction.MOVE_SOUTH;
				} else {
					nextActionMessage += AgentAction.MOVE_NORTH;
				}
			} else {
				nextActionMessage += AgentAction.MOVE_LEFT + ",";
				if (nextAction.getRow() > currentLocation.getRow()) {
					nextActionMessage += AgentAction.MOVE_SOUTH;
				} else {
					nextActionMessage += AgentAction.MOVE_NORTH;
				}
			}
			System.out.println("My next action is " + nextActionMessage);
		}

		return nextActionMessage;
	}

	public String utility(String currentState) {
		String stringToParse[] = currentState.split(":");
		int parseState = 1;
		String action = "";

		for (int i = 0; i < stringToParse.length; ++i) {
			switch (parseState) {
			case 1:
				if (stringToParse[i].equals("response")) {
					parseState++;
				} else {
					System.out.println("Invalid state");
					return "";
				}
				break;
			case 2:
				if (stringToParse[i].equals("actors")) {
					parseState++;
				} else if (stringToParse[i].equals("empty")) {
					parseState = 4;
				} else {
					System.out.println("Invalid state");
					return "";
				}
				break;
			case 3:
				if (stringToParse[i].equals("empty")) {
					parseState++;
					continue;
				} else if (stringToParse[i].equals("end")) {
					return calculateUtility();
				}
				String actorInformation[] = stringToParse[i].split(",");
				Actor actor = null;
				if (actorInformation[0].equals("rock")) {
					actor = new Rock();
				} else if (actorInformation[0].equals("flower")) {
					actor = new Flower();
				} else if (actorInformation[0].equals("critter")) {
					actor = new Critter();
				} else {
					actor = new Bug();
				}
				Location location = new Location(
						Integer.parseInt(actorInformation[1]),
						Integer.parseInt(actorInformation[2]));
				grid.put(location, actor);
				break;
			case 4:
				if (stringToParse[i].equals("end")) {
					return calculateUtility();
				}
				String emptyInformation[] = stringToParse[i].split(",");
				if (emptyInformation.length > 0) {
					Location emptyLocation = new Location(
							Integer.parseInt(emptyInformation[0]),
							Integer.parseInt(emptyInformation[1]));

					grid.remove(emptyLocation);
				} else {
					return calculateUtility();
				}
				break;
			default:
				System.out.println("Invalid state");
				return "";
			}

		}

		return action;
	}

	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msgRx = myAgent.receive();
		if (msgRx == null) {
			block();
			return;
		} else if (msgRx.getPerformative() == ACLMessage.CFP) {
			if (state == PlayingStates.IDLE) {
				state = PlayingStates.EXPLORING;
			} else if (state == PlayingStates.EXPLORING) {

				if(failures > 10)
					randomize = true;
				
				if (randomize) {
					failures--;
					if(failures==0)
						randomize = false;
					ACLMessage msgTx = msgRx.createReply();
					msgTx.setContent("random");
					msgTx.setPerformative(ACLMessage.PROPOSE);
					myAgent.send(msgTx);
				} else {
					ACLMessage msgTx = msgRx.createReply();
					String contentString = msgRx.getContent();
					currentLocation = new Location(
							Integer.parseInt(contentString.split(":")[1]),
							Integer.parseInt(contentString.split(":")[2]));

					System.out.println("I am at (" + currentLocation.getRow()
							+ ", " + currentLocation.getCol() + ")");
					msgTx.setContent("request:around");
					msgTx.setPerformative(ACLMessage.REQUEST);
					myAgent.send(msgTx);
				}
			} else if (state == PlayingStates.PLANNING) {
				String nextActions[] = utility(environmentState).split(":")[1]
						.split(",");

				for (String nextAction : nextActions) {
					nextActionsList.addFirst(nextAction);
				}
				state = PlayingStates.MOVING;
			} else if (state == PlayingStates.MOVING) {
				if (nextActionsList.isEmpty()) {
					state = PlayingStates.EXPLORING;
				} else {
					ACLMessage msgTx = msgRx.createReply();
					msgTx.setContent("action:" + nextActionsList.removeFirst());
					msgTx.setPerformative(ACLMessage.PROPOSE);
					myAgent.send(msgTx);
				}
			}
		} else if (msgRx.getPerformative() == ACLMessage.INFORM) {
			if (state == PlayingStates.EXPLORING) {
				environmentState = msgRx.getContent();
				System.out
						.println("Enviroment information " + environmentState);
				state = PlayingStates.PLANNING;
			}
		} else if (msgRx.getPerformative() == ACLMessage.FAILURE) {
			
			if(!randomize)
				failures++;
			String content = msgRx.getContent();
			String action = content.split(":")[0];

			if (action.equals("moveto")) {
				System.out.println("This is the content " + content);
				int direction = Integer.parseInt(content.split(":")[1]);
				double probability = probabiltyMap.get(direction);
				probabiltyMap.put(direction, probability - 0.1);
				System.out.println("Direction (" + direction + ") failed");

				probability = probabiltyMap.get(AgentAction.MOVE_LEFT);
				probabiltyMap.put(AgentAction.MOVE_LEFT, probability + 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_RIGHT);
				probabiltyMap.put(AgentAction.MOVE_RIGHT, probability + 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_NORTH);
				probabiltyMap.put(AgentAction.MOVE_NORTH, probability + 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_SOUTH);
				probabiltyMap.put(AgentAction.MOVE_SOUTH, probability + 0.025);

				for (Integer key : probabiltyMap.keySet()) {
					System.out.println("Maps " + probabiltyMap.get(key));
				}
			}
		} else if (msgRx.getPerformative() == ACLMessage.CONFIRM) {
			String content = msgRx.getContent();
			String action = content.split(":")[0];

			if (action.equals("moveto")) {
				int direction = Integer.parseInt(content.split(":")[1]);
				System.out.println("Direction (" + direction + ") succeded");
				double probability = probabiltyMap.get(direction);
				probabiltyMap.put(direction, probability + 0.1);

				probability = probabiltyMap.get(AgentAction.MOVE_LEFT);
				probabiltyMap.put(AgentAction.MOVE_LEFT, probability - 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_RIGHT);
				probabiltyMap.put(AgentAction.MOVE_RIGHT, probability - 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_NORTH);
				probabiltyMap.put(AgentAction.MOVE_NORTH, probability - 0.025);
				probability = probabiltyMap.get(AgentAction.MOVE_SOUTH);
				probabiltyMap.put(AgentAction.MOVE_SOUTH, probability - 0.025);
			}
		}
	}
}
