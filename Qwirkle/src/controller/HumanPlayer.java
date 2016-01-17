package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.*;

public class HumanPlayer extends Player {
		
	public HumanPlayer(String name) {
		super(name);
	}

	private String readString(String prompt) {
		System.out.print(prompt + ">");
		//TODO Add comment why we are suppressing this
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String result = scanner.nextLine();
//		scanner.close();
		return result;
	}
	
	@Override
	public ArrayList<Move> determineMove(Board board) {
		String question;
		ArrayList<Move> moves = new ArrayList<Move>();
		String intro = getName() + "'s turn \n"
				  + "Your hand: " + hand.toString() + "\n";
		String drawText = "To draw new tiles: 'draw *tilenr* *tilenr* ...' \n";
		String placeText = "To place tiles: 'place *rownr* *colnr* *tilenr*, repeat \n";
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
					ArrayList<Integer> tilenrs = new ArrayList<Integer>();
					matcher = Pattern.compile(findTilenrs).matcher(answer);
					while (matcher.find()) {
					    tilenrs.add(Integer.parseInt(matcher.group()) - 1);
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
							if (tilenr <= hand.getTiles().size()) {
								moves.add(new Move(row, col, hand.getTiles().get(tilenr)));
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
}