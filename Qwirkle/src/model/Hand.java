package model;

import java.util.ArrayList;

public class Hand {
	
	private ArrayList<Tile> hand = new ArrayList<Tile>();
	
	public Hand(Bag b, int tilesPerPlayer) {
        for (int i = 0; i < tilesPerPlayer; i++) {
            hand.add(b.getBag().get(0));
            b.removeFirst();
        }
	}
	
	public Tile getTile(int tile) {
		return hand.get(tile);
	}
     
	public void removeTile(int tileIndex) {
		hand.remove(tileIndex);
	}
	
	public void addTile(int tileIndex, Tile tile) {
		hand.add(tileIndex, tile);
	}
	
    public String toString() { 
    	return hand.toString(); 
    }

}
