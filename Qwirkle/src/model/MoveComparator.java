package model;

import java.util.*;

public class MoveComparator implements Comparator<ArrayList<Move>>  {
    Map<ArrayList<Move>, Integer> moveMap;
    
    public MoveComparator(Map<ArrayList<Move>, Integer> base) {
        this.moveMap = base;
    }
 
    public int compare(ArrayList<Move> moves1, ArrayList<Move> moves2) {
        if (moveMap.get(moves1) >= moveMap.get(moves2)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
    
	public TreeMap<ArrayList<Move>, Integer> sortByPoints(
			  HashMap<ArrayList<Move>, Integer> map) {
		MoveComparator vc =  new MoveComparator(map);
		TreeMap<ArrayList<Move>, Integer> sortedMap = new TreeMap<ArrayList<Move>, Integer>(vc);
		sortedMap.putAll(map);
		return sortedMap;
	}
}
