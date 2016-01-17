package model;

import java.util.*;
import java.util.Map.Entry;

public class MoveComparator implements Comparator<ArrayList<Move>>  {
    Map<ArrayList<Move>, Integer> moveMap;
    TreeMap<ArrayList<Move>, Integer> sortedResult = null;
    
    public MoveComparator() {
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
		this.moveMap = map;
		TreeMap<ArrayList<Move>, Integer> sortedMap = new TreeMap<ArrayList<Move>, Integer>(this);
		sortedMap.putAll(map);
		sortedResult = sortedMap;
		return sortedMap;
	}
	
	public LinkedHashMap<ArrayList<Move>, Integer> getHeads() {
		LinkedHashMap<ArrayList<Move>, Integer> result = 
					  new LinkedHashMap<ArrayList<Move>, Integer>();
		Entry<ArrayList<Move>, Integer> maxEntry = null;
		if (sortedResult != null) {
			for (Entry<ArrayList<Move>, Integer> entry : sortedResult.entrySet()) {
			    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) == 0) {
			        maxEntry = entry;
			        result.put(entry.getKey(), entry.getValue());
			    } else {
			    	break;
			    }
			}
		}
		return result;
	}
}
