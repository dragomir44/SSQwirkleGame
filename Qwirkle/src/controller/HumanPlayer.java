package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

<<<<<<< HEAD
import model.*;
=======
import view.Game;
>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
	}

<<<<<<< HEAD
	public ArrayList<Move> determineMove(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean correct = false;
		System.out.println(getName() + "'s turn.");
		System.out.println("Your hand: " + hand);
		do {
			String choice = readString("Place a tile or draw new tiles? (draw/place)");
			switch (choice) {
				case "draw":
					drawTiles();
					correct = true;
					break;
				case "place": 
					moves = placeTiles(board);
					correct = true;
					break;
				default:
					System.out.println("you selected " + choice + ". Try again. (draw/place)");
					break;
			}
		} while (!correct);
		return moves;
	}
	
	private void drawTiles() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		boolean valid = false;
		do {
			System.out.println("Your hand: " + hand);
			String choice = readString("> Which tiles do you want to remove? ");
			List<String> allMatches = new ArrayList<String>();
			Matcher m = Pattern.compile("\\d")
			    .matcher(choice);
			while (m.find()) {
			    allMatches.add(m.group());
			}
			for (String tilenr : allMatches) {
				try {
					tiles.add(hand.getHand().get(Integer.parseInt(tilenr) - 1));
				}	catch (IndexOutOfBoundsException e) {
					System.out.println(tilenr + " is outside the range of the hand."
						   + "please select between 1 and " + hand.getHand().size());
					tiles.clear();
					valid = false;
					break;
				}
=======
	public int determineMove(Board board) {
		System.out.println("Your hand: " + this.getHand().toString());		
		String prompt = "> " + getName() 
						+ ", which tile do you want to use?  [Type -1 if you want to switch tiles]";

		boolean valid = false;
		while (!valid) {
			int pieceChoice = readInt(prompt) - 1;
			boolean validPiece = pieceChoice < Game.TILES_PER_HAND && pieceChoice >= 0;
			if (validPiece) {
				System.out.println("> You chose " + this.getHand().getTile(pieceChoice).toString());
			}
			while (!validPiece) {
				if (pieceChoice == -2) {
					String Switch = "> Which tile do you want to switch?";
					pieceChoice = readInt(Switch) - 1;
					validPiece = pieceChoice < Game.TILES_PER_HAND && pieceChoice >= 0;
					if (validPiece) {
						System.out.println("> You chose " 
										+ this.getHand().getTile(pieceChoice).toString());
					}
				} else {
					System.out.println("> *ERROR* Your pieces only go from 1 to 6");
					pieceChoice = readInt(prompt);
					validPiece = pieceChoice <= Game.TILES_PER_HAND && pieceChoice > 0;
					if (validPiece) {
						System.out.println(">You chose " 
										+ this.getHand().getTile(pieceChoice - 1).toString());
					}
				}
			}
			
			String prompt2 = "> In which row you wanna place it? ";
			int rowChoice  = readInt(prompt2);
			boolean validRow = rowChoice <= board.rows && rowChoice >= 0;
			while (!validRow) {
				System.out.println("> *ERROR* The rows only go from 0 to " 
						  + (board.rows - 1) + " right now");
				System.out.println(" *ERROR* The rows only go from 0 to " +
						  (board.rows - 1) + " right now");

				rowChoice = readInt(prompt2);
				validRow = rowChoice <= board.rows && rowChoice >= 0;
			}
			
			String prompt3 = "> In which col you wanna place it? ";
			int colChoice  = readInt(prompt3);
			boolean validCol = colChoice <= board.cols && colChoice >= 0;
			while (!validCol) {
				System.out.println("> *ERROR* The rows only go from 0 to " 
							 + (board.rows - 1) + " right now");
				System.out.println(" *ERROR* The rows only go from 0 to " + 
							  (board.rows - 1) + " right now");
				colChoice = readInt(prompt3);
				validCol = colChoice <= board.cols && colChoice >= 0;
>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb
			}
			if (!tiles.isEmpty()) {
				System.out.println("Removing " + tiles + " from hand, and placing in bag.");
				System.out.println("Drew " + hand.replaceTiles(tiles));
				valid = true;
			} else {
				System.out.println("You have to replace at least 1 tile.");
			}
		} while (!valid);
	}
	
<<<<<<< HEAD
	private ArrayList<Move> placeTiles(Board board) {
		ArrayList<Tile> tmpHand =  new ArrayList<Tile>(this.hand.getHand());
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean makingMove = true;
		while (makingMove) {
			boolean valid = false;
			while (!valid) {
				int pieceChoice = getPiece(tmpHand);
				int rowChoice = getPosition(board, "row");
				int colChoice = getPosition(board, "column");
=======
>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb

				Tile tileToPlace = tmpHand.get(pieceChoice - 1);
				Move newMove = new Move(rowChoice, colChoice, tileToPlace);
				moves.add(newMove);
				if (board.isValidMove(moves)) {
					valid = true;
					tmpHand.remove(tileToPlace);
				} else {
					System.out.println("This is not a valid move.");
					moves.remove(newMove);
					boolean correct = false;
					do {
						String choice = readString("> Try again, reset moves, "
									  + "draw new tiles or end turn"
								  + "(try/reset/draw/end)");
						switch (choice) {
							case "try":
								correct = true;
								break;
							case "draw": 
								drawTiles();
								valid = true;
								moves.clear();
								correct = true;
								break;
							case "end":
								if (!moves.isEmpty()) {
									valid = true;
									correct = true;
								} else {
									System.out.println("No tile has been placed, " + 
												 "Choose try/reset/draw.");	
								}
								break;
							case "reset":
								moves.clear();
								tmpHand =  new ArrayList<Tile>(this.hand.getHand());
								correct = true;
								break;
							default:
								System.out.println("you selected " + choice 
												+ ". Choose: try/reset/draw/end.");
								break;
						}
					} while (!correct);

				}
			}
			boolean correct = false;
			do {
				String choice = readString("Place another tile? (y/n or reset to reset moves)");
				switch (choice) {
					case "y":
						System.out.println("current board layout:");
						System.out.println(board.toString(moves));
						correct = true;
						break;
					case "n": 
						makingMove = false;
						correct = true;
						break;
					case "reset":
						moves.clear();
						correct = true;
						break;
					default:
						System.out.println("you selected " + choice + ". Try again.");
						break;
				}
			} while (!correct);
		}
		return moves;
	}
	private int getPiece(ArrayList<Tile> hand) {
		System.out.print("Your hand: ");
		for (int i = 0; i < hand.size(); i++) {
			int nr = i + 1;
			System.out.print(nr + ":" + hand.get(i) + " ");
		}
		System.out.println();
		String prompt = "> " + getName() + ", which tile you want to place? ";
		int pieceChoice = readInt(prompt);
		boolean validPiece = pieceChoice <= hand.size() && pieceChoice > 0;
		while (!validPiece) {
			System.out.println(" *ERROR* Your pieces only go from 1 to " + hand.size());
			pieceChoice = readInt(prompt);
			validPiece = pieceChoice <= hand.size() && pieceChoice > 0;
		}
		System.out.println("Selected tile " + hand.get(pieceChoice - 1));
		return pieceChoice;
	}
	
	private int getPosition(Board board, String rowOrCol) {
		String prompt = "> In which " + rowOrCol + " do you want to place it? ";
		int posChoice  = readInt(prompt);
		boolean validPos = posChoice <= board.rows && posChoice >= 0;
		while (!validPos) {
			System.out.println(" *ERROR* The " + rowOrCol + " only go from 0 to " +
					  (board.rows - 1) + " right now");

			posChoice = readInt(prompt);
			validPos = posChoice <= board.rows && posChoice >= 0;
		}
		return posChoice;
	}

    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        @SuppressWarnings("resource")
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                }
            }
        } while (!intRead);
        return value;
    }
	
	private String readString(String prompt) {
		System.out.print(prompt);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String result = scanner.nextLine();
		return result;
	}
}