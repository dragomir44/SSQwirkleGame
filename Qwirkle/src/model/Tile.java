package model;


public class Tile {
    
	//Square, Circle, Star, Diamond, Flower, Jew, Empty
	public enum Shape {
		$, O, X, R, G, B,
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
    
    public Tile(int shape, int colour) {
    	this.shape = getShapeByInt(shape);
    	this.colour = getColourByInt(colour);
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
    
    public Shape getShapeByInt(int i) {
    	if (i == 1) {
    		return Shape.$;
    	} else if (i == 2) {
    		return Shape.O;
    	} else if (i == 3) {
    		return Shape.X;
    	} else if (i == 4) {
    		return Shape.R;
    	} else if (i == 5) {
    		return Shape.G;
    	} else if (i == 6) {
    		return Shape.B;
    	} else {
    		return null;
    	}
    }
    
    public Colour getColourByInt(int i) {
    	if (i == 1) {
    		return Colour.R;
    	} else if (i == 2) {
    		return Colour.B;
    	} else if (i == 3) {
    		return Colour.G;
    	} else if (i == 4) {
    		return Colour.Y;
    	} else if (i == 5) {
    		return Colour.O;
    	} else if (i == 6) {
    		return Colour.P;
    	} else {
    		return null;
    	}
    }
    
}
	
