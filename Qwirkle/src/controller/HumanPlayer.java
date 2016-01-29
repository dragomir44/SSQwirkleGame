package controller;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.*;

public class HumanPlayer extends Player {
	
	//@ requires name != null;
	public HumanPlayer(String name) {
		super(name);
	}

	//@ requires prompt != null;
	//@ ensures \result != null;
	/*@ pure */protected String readString(String prompt) {
		writeString(prompt + ">");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String result = scanner.nextLine();
		return result;
	}
	
	@Override
	//@ requires board != null;
	//@ ensures \result != null;
	/*@ pure */public ArrayList<Move> determineMove(Board board) {
		String question;
		ArrayList<Move> moves = new ArrayList<Move>();
		String intro = "Your hand: " + hand.toString() + "\n";
		String drawText = "To draw new tiles: 'draw *tilenr* *tilenr* ...' \n";
		String placeText = "To place tiles: 'place *rownr* *colnr* *tilenr*, repeat \n";
		String invalidEntry = "This is an invalid command, please try again. \n";
		String retry = "Please try again, ";
		
		// Matchers:
		String findCommand = "^([\\w\\-]+)";
		String findTilenrs = "([1-9][0-9]*)";
		String findMovenrs = "(-?[0-9]+)";

		question = intro + drawText + placeText;
		boolean valid = false;
		do {
			TreeMap<ArrayList<Move>, Integer> possibleMoves = 
							 board.getPossibleMoves(hand.getTiles());
			question += "There are " + possibleMoves.size() + " possible moves.\n";
			question += "Hint: ";
			if (!possibleMoves.isEmpty()) { // make sure there is atleast 1 possible move.

				question += board.getPossibleMoves(hand.getTiles()).firstKey().toString() + "\n";
			} else {
				question += "Trade some tiles\n";
			}
			String answer = readString(question);
			List<String> allMatches = new ArrayList<String>();
			Matcher matcher = Pattern.compile(findCommand).matcher(answer);
			while (matcher.find()) {
			    allMatches.add(matcher.group());
			}
			// check for valid syntax
			String command = allMatches.get(0);
			switch (command) {
				case "draw":
					if (board.isEmpty()) {
						question = "You can't trade tiles on the first turn.\n";
						question += retry + drawText + placeText;
					} else {
						Set<Integer> tileNrs = new HashSet<Integer>();
						matcher = Pattern.compile(findTilenrs).matcher(answer);
						while (matcher.find()) {
							int tileNr = Integer.parseInt(matcher.group()) - 1;
							if (tileNr <= hand.getSize()) {
								tileNrs.add(tileNr);
							} else {
								question = "Tile " + tileNr + " does not exist";
								question += retry + drawText + placeText;
								break;
							}

						}
						moves.add(new TradeMove(tileNrs));
						if (moves.size() > 0) {
							valid = true;
						} else {
							question = "You have to trade at least 1 tile.\n";
							question += retry + drawText + placeText;
						}
					}
					break;
				case "place":
					// flag to make sure longest line is placed in first turn
					boolean validFirstMove = true; 
					ArrayList<Integer> movenrs = new ArrayList<Integer>();
					matcher = Pattern.compile(findMovenrs).matcher(answer);
					while (matcher.find()) {
						movenrs.add(Integer.parseInt(matcher.group()));
					}
					if ((movenrs.size() % 3) == 0 && movenrs.size() > 0) { // correct command size
						for (int i = 0; i < movenrs.size(); i = i + 3) { // loop over moves
							int row = movenrs.get(i);
							int col = movenrs.get(i + 1);
							int tilenr = movenrs.get(i + 2) - 1;
							if (tilenr <= hand.getTiles().size()) { // check if tile exists
								try {
									moves.add(new Move(row, col, hand.getTiles().get(tilenr)));
								} catch (ArrayIndexOutOfBoundsException e) {
									determineMove(board);
								}
							} else {
								moves.clear();
								question = "Invalid command," + tilenr + " is not a tilenumber \n";
								question += retry + drawText + placeText;
								break;
							}
						}
						if (board.isEmpty()) { // make sure longest move is played in first turn
							int firstMoveSize = board.getPossibleMoves(
										  hand.getTiles()).firstKey().size();
							if (firstMoveSize != moves.size()) {
								validFirstMove = false;
							}
						}
						if (validFirstMove) {
							if (board.isValidMove(moves)) {
								valid = true;
							} else {
								moves.clear();
								question = "Invalid move " + board.getErrors();
								question += retry + drawText + placeText;
							}
						} else {
							question = "In the first turn the longest row possible "
									+ "has to be placed,";
							question += retry + drawText + placeText;
						}
					} else { // invald syntax
						question = "Not enough input arguments\n";
						question += retry + drawText + placeText;
					}
					break;
				default:
					question = invalidEntry + drawText + placeText;
					break;
			}
		} while (!valid);
		return moves;
	}
}