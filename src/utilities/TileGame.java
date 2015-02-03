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

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import java.awt.Color;
import java.util.Random;

public class TileGame extends World<Tile> {
	public TileGame() {
		Color colors[] = {Color.BLUE, Color.GRAY, Color.BLACK};
		TILE_TYPES types[] = {TILE_TYPES.TILE, TILE_TYPES.OBSTACLE, TILE_TYPES.HOLE};
		int num_available_spaces = 99;
		int amount;
		
		for (int i = 0; i< 3; i++) {
			amount = (int)(new Random().nextDouble() * (num_available_spaces / 6.0));
			num_available_spaces -= amount;
			for (int j = 0; j < amount; j++)
				add(getRandomEmptyLocation(), new Tile(colors[i], types[i]));
		}
		
		first = true;
	}

	public boolean locationClicked(Location loc) {
		Grid<Tile> gr = getGrid();
		Tile t = gr.get(loc);
		
				
		if (t != null) {
			t.flip();
			if (first) {
				if (firstTile != null) {
					firstTile.flip();
					secondTile.flip();
				}
				firstTile = t;
				setMessage("Click on the second tile");
				first = false;
			} else {
				if (firstTile.getColor().equals(t.getColor())) {
					firstTile = secondTile = null;
				} else
					secondTile = t;
				setMessage("Click on the first tile");
				first = true;
			}
		} else {
			add(loc, new Tile(Color.RED, TILE_TYPES.AGENT));
		}
		return true;
	}

	public boolean keyPressed(String description, Location loc) {
		if (description.equals("SPACE"))
			return locationClicked(loc);
		else
			return false;
	}

	public static void main(String[] args) {
		new TileGame().show();
	}

	private Tile firstTile;
	private Tile secondTile;
	private boolean first;
}