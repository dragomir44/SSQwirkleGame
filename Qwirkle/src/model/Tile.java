package model;

import java.util.ArrayList;


public class Tile {
    
	//Square, Circle, Star, Diamond, Flower, Jew, Empty
	public enum Shape {
		$ ("$"),
		O ("O"),
		X ("X"),
		ƒ ("ƒ"),
		Ø ("Ø"),
		¥ ("¥");

		private final String value;
		
		private Shape(final String value) {
			this.value = value;
		}

		// method to return String from enum type
		public String toString() {
			return value;
		}	
	}
	
	//Red, Blue, Green, Yellow, Orange, Purple
	public enum Color {
		R ("red"),
		B ("blue"),
		G ("green"),
		Y ("yellow"), 
		O ("orange"),
		P ("purple");
		
		private final String value;
		
		private Color(final String value) {
			this.value = value;
		}
		
		/* Method to return String from enum
		 * @return string of color
		 */
		public String toString() {
			return value;
		}		
	}
    private final Shape shape;
    private final Color color;
    
    Tile(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Shape getShape() { 
    	return shape; 
    }
    
    public Color getColor() { 
    	return color; 
    }
    
    public String toString() { 
    	return shape + " " + color; 
    }
}
	
