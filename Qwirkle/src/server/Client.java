package server;

import controller.*;
import model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client extends ServerMethods {

	private Board board = new Board();
	private Player player;
	private HashMap<String, Integer> opponents;
	private ArrayList<Move> movesMade; // store the last move that was made
	private InputStreamReader inStream;

	/**
	 * This constructor is used to decide what kind of client is made. A human
	 * or a computer. Here the Server adress can also be defined and the host
	 * port.
     */
	public Client() throws IOException {
//		clientName = getInput("Name:");
//		port = Integer.parseInt(getInput("Port:"));
//		TEST: REMOVE AFTER TESTING
		Random rn = new Random();
//		clientName = "Player" + rn.nextInt(1000);
		clientName = "-S";
		int port = 1337;
		
		switch (clientName) {
			case "-N":
				player = new ComputerPlayer(new NaiveStrategy(), "Fred");
				break;
			case "-S":
				player = new ComputerPlayer(new SmartStrategy(), "Fred");
				break;
			default:
				player = new HumanPlayer(clientName);
				break;
		}

		InetAddress host = InetAddress.getLocalHost();
		opponents = new HashMap<String, Integer>();
		movesMade = new ArrayList<Move>();
		try {
			sock = new Socket(host, port);
	    	in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	    	sendMessage(Protocol.CLIENT_CORE_EXTENSION);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + host
                    + " and port " + port);
		}	
	}
	
	/**
	 * This method is used from the extension of Thread. It runs the client and if
	 * something goes wrong it shuts it down.
     */
	/*@ pure */ public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line != null) {
					readString(line);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		} catch (ServerClientDisaggreeException e) {
			e.printStackTrace();
			shutdown();
		}
	}
	/**
	 * This is the most important method in the class. It received input from the ClientHandler
	 * and translates it to actions or moves. This method follows the BIT Group 2 
	 * Protocol. For every incoming message there is an answer. And if not it just
	 * prints it out.
	 * @param msg the String received from the ClientHandler
	 * @return sendMessage with a protocol message to the ClientHandler
     */
    public synchronized void readString(String msg) throws IOException, 
    ServerClientDisaggreeException {
		System.out.println("Client (" + clientName + ") received:  " + msg);
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
		switch (input[0]) {
			case Protocol.SERVER_CORE_EXTENSION:
				if (clientName == null) { // Verandering
					sendMessage(Protocol.CLIENT_CORE_LOGIN
							  + Protocol.MESSAGESEPERATOR + clientName);
				} else {
					sendMessage(Protocol.CLIENT_CORE_JOIN);
//							  + Protocol.MESSAGESEPERATOR + clientName);
				}
				break;
			case Protocol.SERVER_CORE_LOGIN_DENIED:
				System.out.println("Login denied. Try again with a different name");
				shutdown();
				break;
			case Protocol.SERVER_CORE_LOGIN_ACCEPTED:
				doStartGame(); // request start game input
				break;
			case Protocol.SERVER_CORE_JOIN_DENIED:
				System.out.println("Join denied. Try again");
				shutdown();
				break;
			case Protocol.SERVER_CORE_JOIN_ACCEPTED:
				clientName = input[1];
				System.out.println("Joined server as: " + input[1]);
				doStartGame();

				break;
			case Protocol.SERVER_CORE_START_DENIED:
				System.out.println("Server does not want to start");
				break;
			case Protocol.SERVER_CORE_START:
				for (int i = 1; i < input.length; i++) {
					opponents.put(input[i], 0);
				}
				inStream.close(); // close the start input stream
//				System.out.println(board.toString());
				break;
			// sendMessage(Protocol.CLIENT_CORE_PLAYERS) to ask for players in server
			case Protocol.SERVER_CORE_PLAYERS:
				opponents = new HashMap<>();
				for (int i = 1; i < input.length; i++) {
					String naam = input[i];
					opponents.put(naam, 0);
				}
				break;
			case Protocol.SERVER_CORE_TURN:
				//bepaal current op eigen bord door turn
				String turnPlayer = input[1];
				if (turnPlayer.equals(clientName)) {
					System.out.println("It's your turn!");
					// local player's turn, make a move.
					makeMove();
				} else {
					System.out.println("It's not your turn");
					System.out.println(input[1] + "is playing");
				}
				break;
			case Protocol.SERVER_CORE_MOVE_ACCEPTED:
				// move is accepted, remove this tile from player hand
				System.out.println("Move Accepted");
				if (!movesMade.isEmpty()) {
					Move lastMove = movesMade.get(0);
					try {
						player.getHand().removeTile(lastMove.tile);
					} catch (InvalidTile invalidTile) {
						invalidTile.printStackTrace();
						shutdown();
					}
					movesMade.remove(lastMove);
					if (movesMade.isEmpty()) {
						sendMessage(Protocol.SERVER_CORE_DONE);
					}
				} else {
					throw new ServerClientDisaggreeException("Removing more moves then were made");
				}

				break;
			case Protocol.SERVER_CORE_MOVE_DENIED:
				// current move was invalid, try again.
				System.out.println("Move " + movesMade.get(0) + " was rejected, try again.");
				makeMove();
				break;
			case Protocol.SERVER_CORE_SWAP_ACCEPTED:
				System.out.println("Swap Accepted");
				if (!movesMade.isEmpty()) {
					Move tailMove = movesMade.get(0);
					try {
						player.getHand().removeTile(tailMove.tile);
					} catch (InvalidTile invalidTile) {
						invalidTile.printStackTrace();
						shutdown();
					}
					movesMade.remove(tailMove);
					if (movesMade.isEmpty()) {
						sendMessage(Protocol.SERVER_CORE_DONE);
					}
				} else {
					throw new ServerClientDisaggreeException("Removing more moves then were made");
				}
				break;
			case Protocol.SERVER_CORE_SWAP_DENIED:
				System.out.println("Move " + movesMade.get(0) + " was rejected, try again.");
				makeMove();
				break;
			case Protocol.SERVER_CORE_MOVE_MADE:
				// A player made a valid move, place on board.
				placeTiles(stringToMove(input));
				System.out.println(board.toString());
				break;
			case Protocol.SERVER_CORE_DONE:
				// this is sent after tiles from bag got given to player
				System.out.println("Server done sending tiles");
				System.out.println(board.toString());
				break;
			case Protocol.SERVER_CORE_SCORE:
				addScores(input);
				break;
			case Protocol.SERVER_CORE_SEND_TILE:
				//give this tile to the player
				receiveTiles(stringToTile(input));
				break;
			case Protocol.SERVER_CORE_GAME_ENDED:
				addScores(input);
				System.out.println("The game has ended");
				printResult();
				shutdown();
				break;
			case Protocol.SERVER_CORE_TIMEOUT_EXCEPTION:
				System.out.println("Player <" + input[1] + "> timed out");
				break;
			default:
				System.out.println("Default case!, received: " + msg);
				break;
		}
    }
    
	/*@ requires scoreInput.length() != 0;
	 	ensures player.getScore() > \old(player.getScore())
	 */
    public void addScores(String[] scoreInput) {
		for (int i = 1; i < scoreInput.length; i = i + 2) {
			String name = scoreInput[i];
			int score = Integer.parseInt(scoreInput[i + 1]);
			opponents.put(name, score);
			if (name.equals(clientName)) {
				player.setScore(score);
			}
		}
		printUpdate();
    }
    
	/**
	 * This method asks for input from the client when he wants to start a game.
	 * The Client has to type "Y" to start a game
     */
    /*@ pure */ public void doStartGame() throws IOException {
	    String input = null;
        System.out.println("Type 'Y' to start a game");
		inStream = new InputStreamReader(System.in);
        input = new BufferedReader(inStream).readLine().trim();
		if (input.equals("Y")) {
			sendMessage(Protocol.CLIENT_CORE_START);
		}
    }
	
	/**
	 * Places the moves on your own board
	 * @param move the move that is going to be placed
     */
	//@ requires move.isEmpty() != False;
	public void placeTiles(Move move) {
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		placeTiles(moves);
	}

	/**
	 * Sets the received ArrayList moves into the board. 
	 * Also checks if it's valid or not.
	 * @param moves the moves to be placed on the board
     */
	public void placeTiles(ArrayList<Move> moves) {
		board.setField(moves);
	}
	
	/**
	 * Adds the received Tile into the hand of this Client.
	 * @param tile the tile to be added into the hand
     */
	public void receiveTiles(Tile tile) {
		player.getHand().addTile(tile);
	}

	/**
	 * This method checks if its a TradeMove or a placed move. 
	 * Trades the moves if its a TradeMove
    */
	public void makeMove() {
		movesMade.clear(); // redundand clear
		printUpdate();
		movesMade = player.determineMove(board);
		if (movesMade.get(0) instanceof TradeMove) { // decided to trade tiles
			Set<Integer> tileNrs = ((TradeMove) movesMade.get(0)).tileNrs;
			ArrayList<Tile> tradeTiles = new ArrayList<Tile>();
			for (int nr : tileNrs) {
				tradeTiles.add(player.getHand().getTiles().get(nr));
			}
			swapMove(tradeTiles);
		} else {
			placeMove(movesMade);
		}
	}
	
	/**
	 * Sends a message to the ClientHandler with the tiles you want to be swapped
	 * out of your hand.
	 * @param tiles the tiles you want to be swapped
	 * @return Message to the client with the tiles
     */
	//@ requires tiles.isEmpty() != False;
	/*@ pure */ public void swapMove(ArrayList<Tile> tiles) {
		for (Tile tile : tiles) {
			sendMessage(swapToString(tile));
		}
	}

	/**
	 * Places the moves on a board. Also checks if it's the first move or not
	 * Also sends messages to the ClientHandler about the moves to be placed
	 * @param moves the moves you want to place
	 * @return A message for every single  move to the ClientHandler
     */
	//@ requires moves.isEmpty() != False;
	public void placeMove(ArrayList<Move> moves) {
		Move tailMove = moves.get(0);
		if (board.isEmpty() &&
			   tailMove.row != 0 &&
			   tailMove.col != 0) {
			// first move not on 0,0
			System.out.println("Invalid move, the first should be on 0, 0");
			makeMove(); // retry.
		} else {
			for (Move move : moves) {
				sendMessage(moveToString(move));
			}
		}
	}

	/**
	 * Calculated the bag size based on the tiles dealt and the number of players
	 * @return tiles Amount of tiles left in the bag
     */
	/*@ ensures \result >= 0;
	 */
	/*@ pure */ private int getBagSize() {
		int tiles = 108 - opponents.size() * 6 - board.getTiles().size();
		if (tiles < 0) {
			tiles = 0;
		}
		return tiles;
	}

	/**
	 * This is the method that shows the Client the current game situation.
	 * Basically the TUI.
	 * @return Prints the current game situation in the console
     */
	/*@ pure */ private void printUpdate() {
		System.out.println("\ncurrent game situation:");
		System.out.println(getBagSize() + " tiles left in the bag.");
		for (Map.Entry<String, Integer> playerInServer : opponents.entrySet()) {
			Integer score = playerInServer.getValue();
			String name = playerInServer.getKey();
			System.out.println(name + "'s score is: " + score);
		}
	}

	/**
	 * After the game has ended, this method prints who the winner is and with how
	 * many point they have won.
	 * @return Endresults of the game.
     */
	/*@ pure */ private void printResult() {
		ValueComparator scoreComp = new ValueComparator();
		TreeMap<String, Integer> sortedScores = scoreComp.sortByValue(opponents);

		LinkedHashMap<String, Integer> winners = scoreComp.getHeads();
		if (winners.size() > 1) {
			System.out.println("There is a draw: ");
		} else {
			System.out.print("The winner is: ");
		}
		for (Map.Entry<String, Integer> winner : winners.entrySet()) {
			Integer score = winner.getValue();
			String name = winner.getKey();
			System.out.println(name + " with a score of " + score);
		}
		System.out.println("The rest: ");
		int i = 0;
		for (Map.Entry<String, Integer> playerInScore : sortedScores.entrySet()) {
			Integer score = playerInScore.getValue();
			String name = playerInScore.getKey();
			i++;
			if (i > winners.size()) {
				System.out.println(i + ": " + name + " scored " + score);
			}
		}
	}


	/*@ pure */ public static void main(String[] args) throws IOException {
        Client c1 = new Client();
        c1.start();
    }
}