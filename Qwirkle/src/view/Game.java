package view;

import java.util.*;
import java.util.Map.Entry;
import model.*;

import controller.Board;
import controller.Player;

import static java.lang.Integer.min;

public class Game {

	public static final int TILES_PER_HAND = 6;
	public static final int TILES_PER_BAG = 108;
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
			if (bag.getBag().isEmpty() &&
					!board.hasPossibleMoves(players[current].getHand().getTiles())) {
				System.out.println("No move possible, skipped a turn");
			} else {
				//TODO koopel bag los van player
				String retry = " please try again, ";
				String resultString = "";
				boolean validMove = false;
				boolean validFirstMove = true;
				Player curPlayer = players[current];
				curPlayer.writeString(curPlayer.getName() + "'s turn:");
				do {
					ArrayList<Move> moves = curPlayer.determineMove(board);
					if (!moves.isEmpty()) {	// trade tiles
						if (moves.get(0) instanceof TradeMove) {
							Set<Integer> tileNrs = ((TradeMove) moves.get(0)).tileNrs;
							 if (tileNrs.size() <= getBagSize()) {
								ArrayList<Tile> replacements = curPlayer.getHand().replaceTiles(tileNrs);
								if (replacements != null) {
									resultString = "Drew: " + replacements;
									validMove = true;
								} else {
									resultString = "Invalid trade," + retry;
								}
							} else {
								resultString = "Not enough tiles in bag to trade," + retry;
							}
						} else { // place tiles
							if (board.isValidMove(moves)) {
								resultString = curPlayer.getName() + " scored "
										+ board.getPoints(moves) + " points.";
								resultString += "\n End of turn.";
								for (Move move : moves) {
									curPlayer.getHand().removeTile(move.tile);
								}
								board.setField(moves);
								int points = board.getPoints(moves);
								curPlayer.incrementScore(points);
								validMove = true;
							}
						}
					} else {
						resultString = "No moves were given," + retry;
					}
					curPlayer.writeString(resultString);
				} while (!validMove);

			}
			update();
			current = (current + 1) % numberOfPlayers;

		}
		System.out.println("Game over!");
	}

	private int getBagSize() {
		int tiles = TILES_PER_BAG - numberOfPlayers * TILES_PER_HAND - board.getTiles().size();
		if (tiles < 0) {
			tiles = 0;
		}
		return tiles;
	}

	public boolean gameOver() {
		boolean result = false;
		ArrayList<Tile> allTiles = new ArrayList<Tile>();
		for (Player player : this.players) {
			ArrayList<Tile> playerTiles = player.getHand().getTiles();
			allTiles.addAll(playerTiles);
			if (playerTiles.isEmpty()) {
				player.incrementScore(6); // player that ends game gets 6 extra points
				System.out.println(player.getName() + 
						  " scored 6 extra points for ending the game");
				result = true;
				break;
			}
		}
		allTiles.addAll(bag.getBag());
		if (!board.hasPossibleMoves(allTiles)) {
			System.out.println("No more moves possible");
			result = true; // no more possible moves available

		}
		return result;
	}

	private void update() {
		System.out.println("\ncurrent game situation:");
		System.out.println(bag.getSize() + " tiles left in the bag.");
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