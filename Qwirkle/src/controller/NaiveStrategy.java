package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import model.Hand;
import model.Move;
import model.Tile;

public class NaiveStrategy implements Strategy {

	String name = "Naive";
	Hand hand;
	Random randomGenerator = new Random();
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<Move> determineMove(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean correct = false;
		//get random stones
		//place random stones in found spots
		//if none of the stones fit 
		//switch random tiles
		if (board.firstMove) {
			Move fMove = new Move(board.middleOfBoard, board.middleOfBoard, hand.getHand().get(randomTile()));
			moves.add(fMove);
			board.firstMove = false;
			return moves;
		} else {
			//get open spots
		}
	}
	
	private void drawTiles() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		boolean valid = false;
		do {
			System.out.println("Your hand: " + this.);
			String choice = readString("> Which tiles do you want to remove? (use commas) ");
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
	
	private ArrayList<Move> placeTiles(Board board) {
		ArrayList<Tile> tmpHand =  new ArrayList<Tile>(this.hand.getHand());
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean makingMove = true;
		while (makingMove) {
			boolean valid = false;
			while (!valid) {

				int pieceChoice = getPiece(tmpHand);
				String[] posChoice = getPosition(board);
				int rowChoice = Integer.parseInt(posChoice[0]);
				int colChoice = Integer.parseInt(posChoice[1]);

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
	
	public int randomTile(){
		int randomInt = randomGenerator.nextInt(6);
		return randomInt;
	}

	@Override
	public void setHand(Hand hand) {
		this.hand = hand;
	}
	
	
}
