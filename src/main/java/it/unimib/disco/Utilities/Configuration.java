package it.unimib.disco.Utilities;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	Map<Entry, Integer> components;
	//selection	topk	normalization	proxy

    public enum Entry {SELECTION, TOPK, NORMALIZATION, PROXY};
    public Configuration() {
        components = new HashMap<Entry, Integer>();
    }

    public int get(Entry e) {
        if (components.containsKey(e)) {
            return components.get(e);
        } else {
            return 0;
        }
    }

    public void add(Entry key, Integer s) {
        components.put(key, s);
    }

    public void remove(Entry key) {
        if (components.containsKey(key)) {
            components.remove(key);
        }
    }

    public Configuration copy() {
        Configuration c = new Configuration();
        for (Entry key : components.keySet()) {
            c.add(key, components.get(key));
        }
        return c;
    }
    
    public String toString()
    {
        return components.toString();
    }

}
