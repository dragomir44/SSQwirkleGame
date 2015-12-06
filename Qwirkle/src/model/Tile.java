package model;


public class Tile {
    
	//Square, Circle, Star, Diamond, Flower, Jew, Empty
	public enum Shape {
		$, O, X, ƒ, Ø, ¥,
	}
	
	//Red, Blue, Green, Yellow, Orange, Purple
	public enum Colour {
		R, B, G, Y, O, P
	}
	
    private final Shape shape;
    private final Colour colour;
    
    public Tile(Shape shape, Colour colour) {
        this.shape = shape;
        this.colour = colour;
    }

    public Shape getShape() { 
    	return shape; 
    }
    
    public Colour getColour() { 
    	return colour; 
    }
    
    public String toString() { 
    	return shape + "" + colour; 
    }
}
	
