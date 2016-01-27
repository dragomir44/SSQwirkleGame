package server;

import model.Move;
import model.Tile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public abstract class serverMethods extends Thread {

    protected BufferedReader in;
    protected BufferedWriter out;
    protected String clientName;
    protected Socket sock;

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.err.println("CONNECTION LOST");
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

    public Move stringToMove(String[] moveInput) {
        int x = Integer.parseInt(moveInput[1]);
        int y = Integer.parseInt(moveInput[2]);
        int shape = Integer.parseInt(moveInput[3]);
        int colour = Integer.parseInt(moveInput[4]);
        Tile moveTile = new Tile(shape, colour);
        Move move = new Move(y, x, moveTile);
        return move;
    }

    public String moveToString(Move move) {
        String x = Integer.toString(move.col);
        String y = Integer.toString(move.row);
        String[] tileNumbers = tileToString(move.tile);
        String s = tileNumbers[0]; // shape
        String c = tileNumbers[1];	// color

        String sp = Protocol.MESSAGESEPERATOR;
        String sendStart = Protocol.CLIENT_CORE_MOVE;
 //       String done = Protocol.CLIENT_CORE_DONE;
        return sendStart + sp + x + sp + y + sp + s + sp + c + sp;
    }

    public String swapToString(Tile tile) {
        String[] tileNumbers = tileToString(tile);
        String s = tileNumbers[0]; // shape
        String c = tileNumbers[1];	// color
        String sp = Protocol.MESSAGESEPERATOR;
        String swapStart = Protocol.CLIENT_CORE_SWAP;
        String done = Protocol.CLIENT_CORE_SWAP;

        return swapStart + sp + s + sp + c + sp + done;
    }

    public String[] tileToString(Tile tile) {
        String[] strings = new String[2];
        int count1 = 1;
        for (Tile.Shape shape : Tile.Shape.values()) {
            if (tile.getShape().equals(shape)) {
                strings[0] = Integer.toString(count1);
                break;
            }
            count1++;
        }
        int count2 = 1;
        for (Tile.Colour colour : Tile.Colour.values()) {
            if (tile.getColour().equals(colour)) {
                strings[1] = Integer.toString(count2);
                break;
            }
            count2++;
        }
        return strings;
    }

    public Tile stringToTile(String[] tileInput) {
        Tile tile = new Tile(Integer.parseInt(tileInput[1]), Integer.parseInt(tileInput[2]));
        return tile;
    }
}
