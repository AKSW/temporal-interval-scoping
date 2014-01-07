package it.unimib.disco.Evaluation;

import java.util.HashMap;
import java.util.Map;

public class QualityMeasure {

	 Map<Entry, String> components;

	    public enum Entry {SUBJECT, OBJECT, INTERVAL, GOLDSTANDARD, PRECISION, RECALL, fMEASURE};
	    public QualityMeasure() {
	        components = new HashMap<Entry, String>();
	    }

	    public String get(Entry e) {
	        if (components.containsKey(e)) {
	            return components.get(e);
	        } else {
	            return null;
	        }
	    }

	    public void add(Entry key, String d) {
	        components.put(key, d);
	    }

	    public void remove(Entry key) {
	        if (components.containsKey(key)) {
	            components.remove(key);
	        }
	    }

	    public QualityMeasure copy() {
	    	QualityMeasure m = new QualityMeasure();
	        for (Entry key : components.keySet()) {
	            m.add(key, components.get(key));
	        }
	        return m;
	    }
	    
	    public String toString()
	    {
	        return components.toString();
	    }
}
