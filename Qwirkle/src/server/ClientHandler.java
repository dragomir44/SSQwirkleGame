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
					server.sendMessage(this, clientName + ": " + line);
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
		do {
			switch (input[0]) {
				case Protocol.CLIENT_CORE_EXTENSION:
					sendMessage(Protocol.SERVER_CORE_EXTENSION);
					break;
				case Protocol.CLIENT_CORE_LOGIN:
					if (input[1].contains(" ") || server.getLobby().contains(input[1])) {
						sendMessage(Protocol.SERVER_CORE_LOGIN_DENIED);	
						break;
					} else {
						server.addHandler(handler);
						clientName = input[1];
						sendMessage(Protocol.SERVER_CORE_LOGIN_ACCEPTED);
						break;
					}
				case Protocol.CLIENT_CORE_JOIN:
					Random rn = new Random();
					String clientNo = Integer.toString(rn.nextInt(99) + 1);
					clientName = "Player" + clientNo;
					sendMessage(Protocol.SERVER_CORE_JOIN_ACCEPTED + 
								  Protocol.MESSAGESEPERATOR + clientName);
					break;
					// when Protocol.CLIENT_CORE_JOIN_DENIED?
				case Protocol.CLIENT_CORE_PLAYERS:
					for (int i = 0; i < server.getLobby().size(); i++) {
						sendMessage(Protocol.SERVER_CORE_PLAYERS + Protocol.MESSAGESEPERATOR 
									  + server.getLobby().get(i).getClientName());
					}
					break;
				case Protocol.CLIENT_CORE_START:
					//if enough players --> create game
					//send all clients clients names +  SERVER_CORE_START
					//SERVER_CORE_TURN
					// else SERVER_CORE_START_DENIED
				case Protocol.CLIENT_CORE_MOVE:
//					makeMove(handler, input[1]);
					break;
				case Protocol.CLIENT_CORE_DONE:
					
				case Protocol.CLIENT_CORE_SWAP:

				default:
					System.out.println(msg);
			}
		} while (true);
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