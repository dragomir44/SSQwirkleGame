package model;

import java.util.*;
import java.util.Map.Entry;

public class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;
    TreeMap<String, Integer> sortedResult = null;
    
    public ValueComparator() {
    }
 
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
    
	public TreeMap<String, Integer> sortByValue(Map<String, Integer> map) {
		this.base = map;
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(this);
		sortedMap.putAll(map);
		sortedResult = sortedMap;
		return sortedMap;
	}
	
	public LinkedHashMap<String, Integer> getHeads() {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		Entry<String, Integer> maxEntry = null;
		if (sortedResult != null) {
			for (Entry<String, Integer> entry : sortedResult.entrySet()) {
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
