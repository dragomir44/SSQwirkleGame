package view;

<<<<<<< HEAD
import java.util.*;
import java.util.Map.Entry;
=======
import java.util.Scanner;
>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb

import controller.Board;
import controller.Player;
import model.Bag;
import model.Hand;

public class Game {

	public static final int TILES_PER_HAND = 6;
	private Board board;
	private Bag bag;
	private Player[] players;
	private int current;
	private int numberOfPlayers;
	
	
	public Game(Player[] players)  {
		numberOfPlayers = players.length;
		this.players = players;
	}
	
	public void dealTiles() {
		for (int i = 0; i < numberOfPlayers; i++) {
			players[i].setHand(new Hand(bag, TILES_PER_HAND));
		}
	}

	public void drawNew(Player player, int pieceChoice) {
		player.getHand().removeTile(pieceChoice);
	}
	
	public void start() {
		boolean doorgaan = true;
		while (doorgaan) {
			reset();
			play();
			printResult();
			doorgaan = readBoolean("\n> Play another time? (y/n)?", "y", "n");
		}
	}
	
	private boolean readBoolean(String prompt, String yes, String no) {
		String answer;
		do {
			System.out.print(prompt);
			@SuppressWarnings("resource")
			Scanner in = new Scanner(System.in);
			answer = in.hasNextLine() ? in.nextLine() : null;
		} while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
		return answer.equals(yes);
	}

	private void play() {
		update();
		while (!gameOver()) {
			players[current].makeMove(board);
			update();
			current = (current + 1) % numberOfPlayers;
		}
	}
	
	public boolean gameOver() {
		boolean result = false;
		for (Player player : this.players) {
			if (player.hand.getHand().isEmpty()) {
				player.incrementScore(6); // player that ends game gets 6 extra points
				System.out.println(player.getName() + 
						  " scored 6 extra points for ending the game"); 
				System.out.println("Game over!");
				result = true;
				break;
			}
		}
		return result;
	}

	private void update() {
<<<<<<< HEAD
		System.out.println("\ncurrent game situation:");
		System.out.println(bag.getBag().size() + " tiles left in the bag.");
		for (Player player : players) {
			System.out.println(player.getName() + "'s score is: " + player.getScore());
		}
		
=======
		System.out.println("\ncurrent game situation: \n\n");
		System.out.println("Bag: " + bag.getBag().toString());
		System.out.println("Hand " + players[0].getName() + ":" + players[0].printHand());
		System.out.println("Hand " + players[1].getName() + " :" + players[1].printHand());
>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb
		System.out.println();
		System.out.println(board.toString());
		System.out.println("-------------- Next player -------------");
	}
	
	private void printResult() {
		// TODO take into account a draw
		TreeMap<Integer, String> scores = new TreeMap<Integer, String>();
		for (Player player : players) {
			scores.put(player.getScore(), player.getName());
		}
		int i = 1;
		for (Entry<Integer, String> player : scores.entrySet()) {
		    Integer score = player.getKey();
		    String name = player.getValue();
			if (i == 1) {
				System.out.println("The winner is " + name
				    + " with a score of " + score);
			} else {
				System.out.println(i++ + ": " + name + " scored " + score);
			}
		}
		
	}

	private void reset() {
		board = new Board();
		bag = new Bag();
		dealTiles();
		for (Player player : players) {
			player.setScore(0);
		}
		current = 0;
	}
	
}