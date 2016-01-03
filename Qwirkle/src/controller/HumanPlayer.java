package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.*;

public class HumanPlayer extends Player {
	boolean firstMove = true;
		
	public HumanPlayer(String name) {
		super(name);
	}

	private String readString(String prompt) {
		System.out.print(prompt + ">");
		//TODO Add comment why we are suppressing this
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String result = scanner.nextLine();
		return result;
	}
	
	public ArrayList<Move> determineMove(Board board) {
		String question;
		ArrayList<Move> moves = new ArrayList<Move>();
		String intro = getName() + "'s turn \n"
				  + "Your hand: " + hand.toString() + "\n";
		String drawText = "To draw new tiles: 'draw *tilenr* *tilenr* ...' \n";
		String placeText = "To place tiles: 'place *tilenr* *rownr* *colnr*, repeat \n";
		String invalidEntry = "This is an invalid command, please try again. \n";
		String drawError =  "Invalid tiles, please try again. \n";
		String retry = "Please try again, ";
		String moveErrors = "";
		String resultString = "";
		
		// Matchers:
		String findCommand = "^([\\w\\-]+)";
		String findTilenrs = "([1-9][0-9]*)";
		String findMovenrs = "([1-9][0-9]*)";
		
		question = intro + drawText + placeText;
		boolean valid = false;
		do {
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
					// do something
					ArrayList<Integer> tilenrs = new ArrayList<Integer>();
					matcher = Pattern.compile(findTilenrs).matcher(answer);
					while (matcher.find()) {
					    tilenrs.add(Integer.parseInt(matcher.group()));
					}
					ArrayList<Tile> replacements = hand.replaceTiles(tilenrs);
					if (replacements != null) {
						resultString = "Drew: " + replacements;
						valid = true;
					} else {
						question = drawError + drawText + placeText;
					}
					break;
				case "place":
					// do something
					ArrayList<Integer> movenrs = new ArrayList<Integer>();
					matcher = Pattern.compile(findMovenrs).matcher(answer);
					while (matcher.find()) {
					    movenrs.add(Integer.parseInt(matcher.group()));
					}
					boolean validCommand = true;
					if ((movenrs.size() % 3) == 0 && movenrs.size() > 0) {
						for (int i = 0; i < movenrs.size(); i = i + 3) {
							int row = movenrs.get(i);
							int col = movenrs.get(i + 1);
							int tilenr = movenrs.get(i + 2) - 1;
							// TODO make sure a tile is never used twice..
							if (tilenr <= hand.getHand().size()) {
								moves.add(new Move(row, col, hand.getHand().get(tilenr)));
							} else {
								validCommand = false;
								moves.clear();
								question = "Invalid command," + tilenr + " is not a tilenumber \n";
								question += retry + drawText + placeText;
								break;
							}
						}
						if (validCommand) {
							if (board.isValidMove(moves)) {
								resultString = getName() + " scored "
										+ board.getPoints(moves) + " points.";
								valid = true;
							} else {
								moves.clear();
								moveErrors = board.getErrors();
								question = "Invalid move: " + moveErrors;
								question += retry + drawText + placeText;
							}
						}
					} else { // invald syntax
						question = invalidEntry + drawText + placeText;
					}

					break;
				default:
					question = invalidEntry + retry + drawText + placeText;
					break;
			}
			
		} while (!valid);
		System.out.println(resultString + " end of turn.");
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
	
	
	private String[] getPosition(Board board) {
		String[] posChoice = new String[2];
		if (board.isEmpty() && board.firstMove) {
			posChoice[0] = board.middleOfBoardS;
			posChoice[1] = board.middleOfBoardS;
			board.firstMove = false;
		} else {
			int row = 0;
			int col = 0;
			String prompt = "> In which (row,col) do you want to place it? ";
			posChoice = readString(prompt).split(",");
			try {
				row = Integer.parseInt(posChoice[0]);
				col = Integer.parseInt(posChoice[1]);
			} catch (Exception a) {
				row = -1;
				col = -1;
			}
			boolean validRow = row <= board.rows && row >= 0;
			boolean validCol = col <= board.cols && col >= 0;
			while (!(validRow & validCol)) {
				System.out.println(" *ERROR* The rows only go from 0 to " +
						  (board.rows - 1) + " and the cols go from 0 to " + 
						  (board.cols - 1) + " right now");
		
				posChoice = readString(prompt).split(",");
				try {
					row = Integer.parseInt(posChoice[0]);
					col = Integer.parseInt(posChoice[1]);
				} catch (Exception a) {
					row = -1;
					col = -1;
				}
				validRow = row <= board.rows && row >= 0;
				validCol = col <= board.cols && col >= 0;
			}
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
}