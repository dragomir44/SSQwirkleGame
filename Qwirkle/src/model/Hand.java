package model;

import java.util.*;

public class Hand {
	
	private ArrayList<Tile> hand = new ArrayList<Tile>();
	
	public Hand() {
	}

	public ArrayList<Tile> getTiles() {
		return new ArrayList<Tile>(this.hand);
	}

	public int getSize() {
		return hand.size();
	}

	public void addTiles(ArrayList<Tile> tiles) {
		hand.addAll(tiles);
	}
	
    public String toString() { 
    	String output = "";
		int count = 1;
		for (Tile tile : hand) {
			output += count++ + ": " + tile + " | ";
		}
    	return output;
    }

	public void removeTile(Tile tile) throws InvalidTile {
		if (hand.contains(tile)) {
			hand.remove(tile);
		} else {
			throw new InvalidTile();
		}

	}

	public ArrayList<Tile> removeTiles(Set<Integer> tilenrs) {
		ArrayList<Tile> removedTiles = new ArrayList<Tile>();
		for (int nr : tilenrs) {
			removedTiles.add(hand.get(nr));
		}
		hand.removeAll(removedTiles);
		return removedTiles;
	}

	public void clearHand() {
		hand = new ArrayList<Tile>();
	}

	public boolean isEmpty() {
		return hand.isEmpty();
	}

	public void addTile(Tile tile) {
		hand.add(tile);
	}

}