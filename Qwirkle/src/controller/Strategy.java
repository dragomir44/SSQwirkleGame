package pp.controller;

import pp.model.Mark;

public interface Strategy {
	
	public String getName();
	public int determineMove(Board b, Mark m);

}
