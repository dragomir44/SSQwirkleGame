package controller;

import model.Mark;

public abstract class Player {

	// -- Instance variables -----------------------------------------

	private String name;
	private Mark mark;

	// -- Constructors -----------------------------------------------

	public Player(String theName, Mark theMark) {
		this.name = theName;
		this.mark = theMark;
	}

	// -- Queries ----------------------------------------------------

	public String getName() {
		return name;
	}

	public Mark getMark() {
		return mark;
	}

	public abstract int determineMove(Board board);

	public void makeMove(Board board) {
		int keuze = determineMove(board);
		board.setField(keuze, getMark());
	}

}