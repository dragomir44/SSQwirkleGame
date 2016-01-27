package view;

import controller.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Qwirkle {
	public static void main(String[] a) {
		String[] args = new String[2];
		args[0] = "-S";
		args[1] = "-N";
    	if (args.length > 1 && args.length <= 4) {
    		Player[] players = new Player[args.length];
    		for (int i = 0; i < args.length; i++) {
				String autoname = "Fred" + Integer.toString(i);
    			switch (args[i]) {
    				case "-N":
						players[i] = new ComputerPlayer(new NaiveStrategy(), autoname);
						break;
    				case "-S":
						players[i] = new ComputerPlayer(new SmartStrategy(), autoname);
    					break;
    				default:
    					players[i] = new HumanPlayer(args[i]);
    					break;
    			}
    		}

    		Game game = new Game(new ArrayList<Player>(Arrays.asList(players)));
    		game.start();
    	} else {
    		System.err.println("Wrong input arguments");
    	}
	}
}
