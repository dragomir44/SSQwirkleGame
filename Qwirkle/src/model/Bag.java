package model;

import java.util.ArrayList;
import java.util.Collections;

import model.Tile.Colour;
import model.Tile.Shape;

public class Bag {

	private static final ArrayList<Tile> BAG = new ArrayList<Tile>();
	
    public Bag() {
        for (Colour colour : Colour.values()) {
            for (Shape shape : Shape.values()) {
                BAG.add(new Tile(shape, colour));
            }
        }
        Collections.shuffle(BAG);
    }
    
    public ArrayList<Tile> getBag() {
        return new ArrayList<Tile>(BAG); // Return copy of prototype deck
    }
    
    public void getFirst() {
    	BAG.get(0);
    }
    
    public void removeFirst() {
    	BAG.remove(0);
    }
    
	public static void main(String[] args) {
//		Bag bag = new Bag();
//		System.out.println(bag.BAG);
//		bag.BAG.remove(0);
//		bag.getBag();
//		System.out.println(bag.BAG);
//		bag.BAG.remove(0);
//		System.out.println(bag.BAG);

	}

}
