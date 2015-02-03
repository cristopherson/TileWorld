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

public class TileGame extends World<Actor> {
	public TileGame() {
		Actor actors[] = {new Flower(Color.RED), new Critter(), new Rock()};
		int num_available_spaces = 98;
		int amount;
		
		for (int i = 0; i< 3; i++) {
			amount = (int)(new Random().nextDouble() * (num_available_spaces / 6.0)) + 1;
			num_available_spaces -= amount;
			for (int j = 0; j < amount; j++)
				add(getRandomEmptyLocation(), actors[i]);
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
		new TileGame().show();
	}
	
	public void step() {
		setMessage("This is a random location " + getRandomEmptyLocation());
	}
}