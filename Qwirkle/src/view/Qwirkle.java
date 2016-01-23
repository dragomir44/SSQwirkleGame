package view;

import controller.*;

public class Qwirkle {
	public static void main(String[] a) {
		String[] args = new String[2];
		args[0] = "-N";
		args[1] = "-S";
    	if (args.length > 1 && args.length <= 4) {
    		Player[] players = new Player[args.length];
    		for (int i = 0; i < args.length; i++) {
				String autoname = "Fred" + Integer.toString(i);
    			switch (args[i]) {
    				case "-N":
						players[i] = new ComputerPlayer(new NaiveStrategy(), autoname);
						break;
//    				case "-S":
//						players[i] = new ComputerPlayer(new SmartStrategy(), autoname);
//    					break;
    				default:
    					players[i] = new HumanPlayer(args[i]);
    					break;
    			}
    		}
    		Game game = new Game(players);	
    		game.start();
    	} else {
    		System.err.println("Wrong input arguments");
    	}
	}
}
