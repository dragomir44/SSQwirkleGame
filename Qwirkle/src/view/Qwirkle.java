package view;

import controller.*;

public class Qwirkle {
	public static void main(String[] a) {
		String[] args = new String[2];
		args[0] = "Sergay";
		args[1] = "Frank";
    	if (args.length > 1) {
    		Player[] players = new Player[args.length];
    		for (int i = 0; i < args.length; i++) {
    			switch (args[i]) {
//    				case "-N": players[i] = new ComputerPlayer();
//    							break;
//    				case "-S": players[i] = new ComputerPlayer(new SmartStrategy());
//    							break;
    				default: players[i] = new HumanPlayer(args[i]);
    							break;
    			}
    		}
    		Game game = new Game(players);	
    		game.start();
    	}
	}
}
