package view;

import java.util.*;
import java.util.Map.Entry;
import model.*;

import controller.Board;
import controller.Player;

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

	public void start() {
		boolean doorgaan = true;
		while (doorgaan) {
			reset();
//			board.getTiles().put(7, 7, new Tile(Tile.Shape.X, Tile.Colour.R));
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
			if (player.getHand().getTiles().isEmpty()) {
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
		System.out.println("\ncurrent game situation:");
		System.out.println(bag.getBag().size() + " tiles left in the bag.");
		for (Player player : players) {
			System.out.println(player.getName() + "'s score is: " + player.getScore());
		}
		
		System.out.println();
		System.out.println(board.toString());
		System.out.println("-------------- Next player -------------");
	}
	
	private void printResult() {
		HashMap<String, Integer> scores = new HashMap<String, Integer>();
		for (Player player : players) {
			scores.put(player.getName(), player.getScore());
		}
		ValueComparator scoreComp = new ValueComparator();
		TreeMap<String, Integer> sortedScores = scoreComp.sortByValue(scores);

		LinkedHashMap<String, Integer> winners = scoreComp.getHeads();
		if (winners.size() > 1) {
			System.out.println("There is a draw: ");
		} else {
			System.out.print("The winner is: ");
		}
		for (Entry<String, Integer> winner : winners.entrySet()) {
			Integer score = winner.getValue();
			String name = winner.getKey();
			System.out.println(name + " with a score of " + score);
		}
		System.out.println("The rest: ");
		int i = 0;
		for (Entry<String, Integer> player : sortedScores.entrySet()) {
		    Integer score = player.getValue();
		    String name = player.getKey();
		    i++;
		    if (i > winners.size()) {
		    	System.out.println(i + ": " + name + " scored " + score);
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