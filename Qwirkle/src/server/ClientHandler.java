package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import controller.Player;
import model.Move;
import view.Game;

public class ClientHandler extends Thread {

    private Server server;
    private BufferedWriter out;
    private BufferedReader in;
    private String clientName;
    private Socket socket;
    public boolean rematch;
  

    public ClientHandler(Server serverArg, Socket sock) throws IOException {
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        server = serverArg;
        socket = sock;
    }

    public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line != null) {
					readString(this, line);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
        	System.out.println("CONNECTION LOST");
            shutdown();
        }
    }

    private void shutdown() {
        server.removeHandler(this);
    }
    
    public String getClientName() {
    	if (clientName == null) {
    		return String.valueOf(socket.getInetAddress());
    	} else {
    		return clientName;
    	}
    }
    
    public void announce() throws IOException {
        clientName = in.readLine();
        server.sendMessage(this, "[" + clientName + " has entered]");
    }

    public synchronized void readString(ClientHandler handler, String msg) {
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
    	int x;
    	int y;
    	int shape;
    	int colour;
    	boolean done = false;
		do {
			switch (input[0]) {
				case Protocol.CLIENT_CORE_EXTENSION:
					sendMessage(Protocol.SERVER_CORE_EXTENSION);
					done = true;
					break;
				case Protocol.CLIENT_CORE_LOGIN:
					if (input[1].contains(" ") || server.getLobby().contains(input[1])) {
						sendMessage(Protocol.SERVER_CORE_LOGIN_DENIED);
					} else {
						server.addHandler(handler);
						clientName = input[1];
						sendMessage(Protocol.SERVER_CORE_LOGIN_ACCEPTED);
					}
					done = true;
					break;
				case Protocol.CLIENT_CORE_JOIN:
					Random rn = new Random();
					String clientNo = Integer.toString(rn.nextInt(99) + 1);
					clientName = "Player" + clientNo;
					sendMessage(Protocol.SERVER_CORE_JOIN_ACCEPTED + 
								  Protocol.MESSAGESEPERATOR + clientName);
					done = true;
					break;
					// when Protocol.CLIENT_CORE_JOIN_DENIED?
				case Protocol.CLIENT_CORE_PLAYERS:
					// Lobby or game?
					StringBuilder players = new StringBuilder();
					for (int i = 0; i < server.getLobby().size(); i++) {
						players.append(Protocol.MESSAGESEPERATOR 
									  + server.getLobby().get(i).getClientName());
					}
					sendMessage(Protocol.SERVER_CORE_PLAYERS + players);
					done = true;
					break;
				case Protocol.CLIENT_CORE_START:
					//if enough players --> create game
					//send all clients clients names +  SERVER_CORE_START
					//SERVER_CORE_TURN
					// else SERVER_CORE_START_DENIED
				case Protocol.CLIENT_CORE_MOVE:
					x = Integer.parseInt(input[1]);
					y = Integer.parseInt(input[2]);
					shape = Integer.parseInt(input[3]);
					colour = Integer.parseInt(input[4]);
					// if move = valid
					if (true) {
						//plaats move op server gameboard
						sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
						//stuur alle spelers deze move
					} else {
						// if tile is not in hand of client i.e.
						sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
					}
					// makeMove(handler, input[1]);
					done = true;
					break;
				case Protocol.CLIENT_CORE_DONE:
					// sendMessage(SERVER_CORE_SEND_TILE) met het juiste aantal tiles in Shape en Kleur als integer
					sendMessage(Protocol.SERVER_CORE_DONE);
					//broadcast SERVER_CORE_SCORE met (Naam Integer) paren
					done = true;
					break;
				case Protocol.CLIENT_CORE_SWAP:
					x = Integer.parseInt(input[1]);
					y = Integer.parseInt(input[2]);
					shape = Integer.parseInt(input[3]);
					colour = Integer.parseInt(input[4]);
					// if move = valid
					if (true) {
						//plaats move op server gameboard
						sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
						//stuur alle spelers deze move
					} else {
						// if tile is not in hand of client i.e.
						sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
					}
					done = true;
					break;
				default:
					System.out.println(msg);
					done = true;
			}
		} while (!done);
    }

    public boolean makeMove(ArrayList<Move> moves) {
        // stuur moves naar server
        // lees of ze correct zijn
    	return true;
    }

    // ontvang moves van de server
    public ArrayList<Move> determineMove() {
    	return null;
    }

}