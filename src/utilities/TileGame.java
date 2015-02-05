package utilities;

/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Flower;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import java.awt.Color;
import java.util.Random;

import agents.BugAgent;
import agents.Environment;

public class TileGame extends World<Actor> {
	private Environment env;
	public TileGame(Environment env) {
		this.env = env;
		Actor actors[] = {new Flower(Color.RED),  new Rock(), new Critter()};
		int num_available_spaces = 98;
		int amount;
		
		amount = (int)(new Random().nextDouble() * (num_available_spaces / 6.0)) + 1;
		
		for (int j = 0; j < amount; j++) {
			add(getRandomEmptyLocation(), actors[0]);
			add(getRandomEmptyLocation(), actors[1]);
		}
		
		num_available_spaces -= (amount * 2);		
		amount = (int)(new Random().nextDouble() * (num_available_spaces / 6.0)) + 1;
		
		for (int j = 0; j < amount; j++) {
			add(getRandomEmptyLocation(), actors[2]);
		}
	}
	
	public boolean locationClicked(Location location) {
		Grid<Actor> grid = getGrid();
		Actor actor = grid.get(location);
		
		if (actor == null) {
			add(location, new Bug());
		} else {			
			if (actor.getClass() == Rock.class) {
				setMessage("This is a rock" );
			}
		}
		return true;
	}

	public boolean keyPressed(String description, Location location) {
		if (description.equals("SPACE"))
			return locationClicked(location);
		else
			return false;
	}

	public static void main(String[] args) {
		//new TileGame(null).show();
	}
	
	public void step() {
		for (Location location :env.getMap().values()) {
			BugAgent agent = (BugAgent)this.getGrid().get(location);
			agent.act();
		}
		
	}
}