package controller;

//import model.Mark;
//import model.PlayerTiles

public abstract class Player {

	// -- Instance variables -----------------------------------------

	private String name;
//	private Mark mark;
//	private PlayerTiles playertiles;
	
	// -- Constructors -----------------------------------------------

	public Player(String theName) {
		this.name = theName;
	}

	// -- Queries ----------------------------------------------------


	public String getName() {
		return name;
	}


	public abstract int determineMove(Board board);


	public void makeMove(Board board) {
		int keuze = determineMove(board);
	}

}