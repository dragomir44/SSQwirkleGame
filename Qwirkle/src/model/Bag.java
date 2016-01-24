package model;

import java.util.ArrayList;
import java.util.Collections;

import model.Tile.Colour;
import model.Tile.Shape;

public class Bag {

	private static final ArrayList<Tile> BAG = new ArrayList<Tile>();
	
    public Bag() {
    	for (int i = 0; i < 3; i++) {
	        for (Colour colour : Colour.values()) {
	            for (Shape shape : Shape.values()) {
	                BAG.add(new Tile(shape, colour));
	            }
	        }
	        Collections.shuffle(BAG);
    	}
    }
    
    public ArrayList<Tile> getBag() {
        return BAG;
    }
    
    public void removeFirst() {
    	BAG.remove(0);
    }
    
    public void addTiles(ArrayList<Tile> tiles) {
    	BAG.addAll(tiles);
    	Collections.shuffle(BAG);
    }
}
