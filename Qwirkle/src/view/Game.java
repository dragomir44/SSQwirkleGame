package view;

import java.util.Scanner;

import controller.Board;
import controller.Player;
import model.Bag;
import model.Hand;

public class Game {

	public static final int NUMBER_PLAYERS = 2;
	public static final int TILES_PER_HAND = 6;
	private Board board;
	private Bag bag;
	private Player[] players;
	private int current;
	
	
	public Game(Player p0, Player p1)  {
		board = new Board();
		bag = new Bag();
		players = new Player[NUMBER_PLAYERS];
		players[0] = p0;
		players[1] = p1;
		dealTiles();
		current = 0;
	}
	
	public void dealTiles() {
		for (int i = 0; i < NUMBER_PLAYERS; i++) {
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
//			printResult();
			doorgaan = readBoolean("\n> Play another time? (y/n)?", "y", "n");
		}
	}
	
	private boolean readBoolean(String prompt, String yes, String no) {
		String answer;
		do {
			System.out.print(prompt);
			Scanner in = new Scanner(System.in);
			answer = in.hasNextLine() ? in.nextLine() : null;
		} while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
		return answer.equals(yes);
	}

	private void play() {
		update();
		while (!board.gameOver()) {
			players[current].makeMove(board);
			update();
			current = (current + 1) % 2;
		}
	}

	private void update() {
		System.out.println("\ncurrent game situation: \n\n");
		System.out.println("Bag: " + bag.getBag().toString());
		System.out.println("Hand " + players[0].getName() + ":" + players[0].printHand());
		System.out.println("Hand " + players[1].getName() + " :" + players[1].printHand());
		System.out.println();
		System.out.println(board.toString());
	}
	

//	private void printResult() {
//		if (board.hasWinner()) {
//			Player winner = players[(current + 1) % 2];
//			System.out.println("Speler " + winner.getName() + " has won!");
//		} else {
//			System.out.println("Draw. There is no winner!");
//		}
//	}

	private void reset() {
		current = 0;
//		board.reset();
	}
	
}