package model;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
	
	private ArrayList<Tile> hand;
	
	private Hand(ArrayList<Tile> bag, int cardsPerHand) {
        int bagSize = bag.size();
        java.util.List<Tile> handView = bag.subList(bagSize - cardsPerHand, bagSize);
        this.hand = new ArrayList<Tile>(handView);
		handView.clear();	
	}
     
    public String toString() { 
    	return hand.toString(); 
    }
    
    public static void main(String args[]) {
   //     int numHands = Integer.parseInt(args[0]);
   //     int cardsPerHand = Integer.parseInt(args[1]);
    	int numHands = 2;
    	int cardsPerHand = 6;
    	Bag gbag = new Bag();
        ArrayList<Tile> bag  = gbag.getBag();
        Collections.shuffle(bag);
    	for (int i = 0; i < numHands; i++) {
    		Hand h = new Hand(bag, cardsPerHand);
    		System.out.println(h.toString());
    	}
    }
}
