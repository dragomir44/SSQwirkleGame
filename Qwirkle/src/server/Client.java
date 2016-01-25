package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	private String clientName;
	private int port;
	private InetAddress host;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private BufferedReader playerInput;
	private boolean firstTurn;
	
	public Client() throws IOException {
		clientName = getInput("Name:");
		port = Integer.parseInt(getInput("Port:"));
		host = InetAddress.getLocalHost();
		playerInput = new BufferedReader(new InputStreamReader(System.in));
		firstTurn = true;
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
	
    public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line != null) {
					System.out.println(line);
					//analyze strings
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}
    
	public String getInput(String question) throws IOException {
	    String input = null;
        System.out.println(question);
        input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        return input;
    }
	
	public void sendMessage(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
    }

	public void shutdown() {
		System.out.println("CLOSING SOCKET");
		try {
			sock.close();
			in.close();
			out.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public String getClientName() {
		return clientName;
	}
	
    public static void main(String[] args) throws IOException {
        Client c1 = new Client();
        c1.start();
        
    }
}