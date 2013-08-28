package it.unimib.disco.ReadFiles;

import java.util.HashMap;
import java.util.Map;

public class PredicateObject {
	 Map<Entry, String> components;

	    public enum Entry {PREDICATE, DATE};
	    public PredicateObject() {
	        components = new HashMap<Entry, String>();
	    }

	    public String get(Entry e) {
	        if (components.containsKey(e)) {
	            return components.get(e);
	        } else {
	            return null;
	        }
	    }

	    public void add(Entry key, String s) {
	        components.put(key, s);
	    }

	    public void remove(Entry key) {
	        if (components.containsKey(key)) {
	            components.remove(key);
	        }
	    }
    
	    public String toString()
	    {
	        return components.toString();
	    }
}
