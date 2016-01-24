package model;

import java.util.*;

public class Hand {
	
	private int tilesPerPlayer;
	private Bag bag;
	public ArrayList<Tile> hand = new ArrayList<Tile>();
	
	public Hand(Bag bag, int tilesPerPlayer) {
		this.bag = bag;
		this.tilesPerPlayer = tilesPerPlayer;
		drawTiles();
	}

	public Bag getBag() {
		return bag;
	}

	public ArrayList<Tile> getTiles() {
		return new ArrayList<Tile>(this.hand);
	}
	
    public String toString() { 
    	String output = "";
		int count = 1;
		for (Tile tile : hand) {
			output += count++ + ": " + tile + " | ";
		}
    	return output;
    }
    
	public ArrayList<Tile> removeTiles(ArrayList<Tile> tiles) {
		hand.removeAll(tiles);
		return drawTiles();
	}
	
	public ArrayList<Tile> removeTile(Tile tile) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		return removeTiles(tiles);
	}

	public ArrayList<Tile> replaceTiles(ArrayList<Integer> tileIndex) {
		Set<Integer> tilenrs = new HashSet<Integer>(tileIndex);
		return replaceTiles(tilenrs);
	}

	public ArrayList<Tile> replaceTiles(Set<Integer> tilenrs) {
		ArrayList<Tile> newTiles = new ArrayList<Tile>(tilenrs.size());
		ArrayList<Tile> oldTiles = new ArrayList<Tile>(tilenrs.size());
		if (Collections.min(tilenrs) < 0 ||
			   Collections.max(tilenrs) > hand.size() ||
			   tilenrs.size() > bag.getBag().size()) {
			newTiles = null;
		} else {
			for (int i : tilenrs) {
				oldTiles.add(hand.get(i));
			}
			newTiles = removeTiles(oldTiles);
			bag.addTiles(oldTiles);
		}
		return newTiles;
	}
	
	private ArrayList<Tile> drawTiles() {
		ArrayList<Tile> tilesDrawn = new ArrayList<Tile>();
		int tilesToDraw = tilesPerPlayer - hand.size();
        for (int i = 0; i < tilesToDraw; i++) {
        	if (!bag.getBag().isEmpty()) {
        		Tile drawnTile = bag.getBag().remove(0);
        		tilesDrawn.add(drawnTile);
        		hand.add(drawnTile);	
        	}
        }
        return tilesDrawn;
	}
}