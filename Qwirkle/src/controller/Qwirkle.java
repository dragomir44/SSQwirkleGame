package controller;

import view.Game;

public class Qwirkle {

	public static void main(String[] args) {
		boolean test = true;

		Game game = new Game(new HumanPlayer("Sergey"), new HumanPlayer("Frank"));
		game.start();
	}
		
}
