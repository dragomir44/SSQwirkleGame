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
	private ArrayList<Player> players;
	private int current;
	private int numberOfPlayers;
	
	
	public Game(ArrayList<Player> players)  {
		numberOfPlayers = players.size();
		this.players = players;
	}
	
	public void dealTiles() {
		for (Player player : players) {
			replenishTiles(player);
		}
	}

	public void AddPlayer(Player player) {
		players.add(player);
		numberOfPlayers++;
	}


	public ArrayList<Tile> replenishTiles(Player p) {
		ArrayList<Tile> newTiles = new ArrayList<Tile>();
		int TilesToDraw = TILES_PER_HAND - p.getHand().getSize();
		// make sure to never draw more then bag size
		if (TilesToDraw > bag.getSize()) {
			TilesToDraw = bag.getSize();
		}
		for (int i = 0; i < TilesToDraw; i ++) {
			Tile drawnTile = bag.getBag().remove(0);
			newTiles.add(drawnTile);
		}
		p.getHand().addTiles(newTiles);
		return newTiles;
	}

	public ArrayList<Tile> replaceTiles(Player p, Set<Integer> tilenrs) {
		ArrayList<Tile> drawnTiles = new ArrayList<Tile>();
		Hand hand = p.getHand();
		int bagSize = bag.getBag().size();
		ArrayList<Tile> newTiles = new ArrayList<Tile>(tilenrs.size());
		ArrayList<Tile> oldTiles = new ArrayList<Tile>(tilenrs.size());
		if (tilenrs.size() > bag.getSize()) {
			System.err.println("Drew more tiles then available in bag");
		}
		if (Collections.min(tilenrs) < 0 || // check for unrealistic tile numbers
				Collections.max(tilenrs) >= hand.getSize()) {
			System.err.println("Unrealistic tile numbers: " + tilenrs);
		} else {
			oldTiles = hand.removeTiles(tilenrs); // remove tiles from hand
			bag.addTiles(oldTiles); // place removed tiles in bag
		}
		drawnTiles = replenishTiles(p);
		return drawnTiles;
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

	public void play() {
		update();
		while (!gameOver()) {
			if (bag.getBag().isEmpty() &&
					!board.hasPossibleMoves(players.get(current).getHand().getTiles())) {
				System.out.println("No move possible, skipped a turn");
			} else {
				boolean validMove = false;
				Player curPlayer = players.get(current);
				curPlayer.writeString(curPlayer.getName() + "'s turn:");
				do {
					ArrayList<Move> moves = curPlayer.determineMove(board);
					validMove = makeMove(curPlayer, moves);
				} while (!validMove);

			}
			update();
			current = (current + 1) % numberOfPlayers;

		}
		System.out.println("Game over!");
	}

	public boolean makeMove(Player curPlayer, ArrayList<Move> moves) {
		String retry = " please try again, ";
		String resultString = "";
		boolean validMove = false;

		if (!moves.isEmpty()) {	// trade tiles
			if (moves.get(0) instanceof TradeMove) {
				Set<Integer> tileNrs = ((TradeMove) moves.get(0)).tileNrs;
				if (tileNrs.size() <= getBagSize()) {
					ArrayList<Tile> replacements = replaceTiles(curPlayer, tileNrs);
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
					replenishTiles(curPlayer);
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
		return validMove;
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
		for (Player player : players) {
			player.setScore(0);
		}
		dealTiles();
		

		current = 0;
	}
	
}