
package tests;

import model.*;
import controller.*;

import java.util.*;


import org.junit.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleUnitTests {
	private Board board;
	private Tile tile;
	private Tile tile1;
	private Tile tile2;
	private Tile tile3;
	private Tile tile4;
	private Tile tile5;
	private Tile tile6;
	private Tile tile7;
	private Tile tile8;
	private Tile tile9;
	private Tile tile10;
	private Tile tile11;
	private Tile tile12;
	private Tile tile13;
	
	@Before
	public void setUp() {
		board = new Board();
		tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
		tile2 = new Tile(Tile.Shape.X, Tile.Colour.G);
		tile3 = new Tile(Tile.Shape.X, Tile.Colour.P);

		tile10 = new Tile(Tile.Shape.X, Tile.Colour.O);
		tile4 = new Tile(Tile.Shape.X, Tile.Colour.Y);
		tile5 = new Tile(Tile.Shape.B, Tile.Colour.R);
		tile6 = new Tile(Tile.Shape.R, Tile.Colour.G);
		tile7 = new Tile(Tile.Shape.R, Tile.Colour.G);
		tile8 = new Tile(Tile.Shape.B, Tile.Colour.R);
		tile9 = new Tile(Tile.Shape.X, Tile.Colour.G);
	}
//
//	@Test
//	public void testTileOnCorner() {
//		Move move1 = new Move(0, 0, tile);
//		ArrayList<Move> moves = new ArrayList<Move>();
//		moves.add(move1);
//		assertTrue(board.isValidMove(moves));
//	}
	
	@Test
	public void testValidMove() {
		Move move1 = new Move(4, 5, tile);
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move1);
		assertTrue(board.isValidMove(moves));
		
		Move move2 = new Move(4, 6, tile1);
		Move move3 = new Move(4, 7, tile2);
		moves.add(move2);
		moves.add(move3);
		assertTrue(board.isValidMove(moves));
		System.out.println(board.getErrors());
		
		assertTrue(board.setField(moves));
		// double color
		Move move4 = new Move(4, 8, tile);
		moves.clear();
		moves.add(move4);
		assertFalse(board.setField(moves));

		// same color as neighbour but wrong line shape
		Move move5 = new Move(4, 8, tile7);
		moves.clear();
		moves.add(move5);
		assertFalse(board.setField(moves));
		board.getTiles().clear();

	}

	@Test
	public void testMergeRow() {
		ArrayList<Move> moves = new ArrayList<Move>();
		board.getTiles().put(0, 1, tile1);
		board.getTiles().put(0, 2, tile2);
		board.getTiles().put(0, 3, tile3);
		board.getTiles().put(0, 4, tile);
		board.getTiles().put(0, 6, tile1);

		Move move6 = new Move(0, 5, tile10);
		moves.clear();
		moves.add(move6);
		System.out.println(board.toString());
		assertFalse(board.setField(moves));
	}

//	@Test
//	public void testPoints() {
//		Move move1 = new Move(4, 5, tile);
//		Move move2 = new Move(4, 6, tile1);
//		Move move3 = new Move(4, 7, tile2);
//		Move move4 = new Move(4, 8, tile3);
//		Move move5 = new Move(5, 8, tile4);
//
//		ArrayList<Move> moves = new ArrayList<Move>();
//		moves.add(move1);
//		moves.add(move2);
//		moves.add(move3);
//		moves.add(move4);
//
//		board.setField(moves);
//		assertEquals(4, board.getPoints(moves));
//
//		moves.clear();
//		moves.add(move2);
//		moves.add(move3);
//		board.setField(moves);
//		assertEquals(3, board.getPoints(moves));
//
//		moves.clear();
//		moves.add(move4);
//		moves.add(move5);
//		assertTrue(board.setField(moves));
//		assertEquals(6, board.getPoints(moves));
//	}
//
//
//	@Test
//	public void movePredicter() {
//		ArrayList<Move> moves = new ArrayList<Move>();
//		Move move1 = new Move(4, 5, tile);
//		Move move2 = new Move(4, 6, tile1);
//		Move move3 = new Move(4, 7, tile2);
//		Move move4 = new Move(4, 8, tile3);
//		Move move5 = new Move(5, 8, tile4);
//		moves.add(move1);
//		board.setField(moves);
//		ArrayList<Tile> useTiles = new ArrayList<Tile>();
//		useTiles.add(tile7);
//		useTiles.add(tile6);
//		TreeMap<ArrayList<Move>, Integer> possibleMoves = board.getPossibleMoves(useTiles);
//
////		for (Entry<ArrayList<Move>, Integer> metaMove : possibleMoves.entrySet()) {
////			ArrayList<Move> moveList = metaMove.getKey();
////			int points = metaMove.getValue();
////			for (Move move : moveList) {
////				System.out.print(move.toString() + " | ");
////			}
////			System.out.println(" worth " + points);
////		}
//
//	}

}