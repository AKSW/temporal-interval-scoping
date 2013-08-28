package it.unimib.disco.Evaluation;

import java.util.HashMap;
import java.util.Map;

public class QualityMeasure {

	 Map<Entry, Double> components;

	    public enum Entry {PRECISION, RECALL, fMEASURE};
	    public QualityMeasure() {
	        components = new HashMap<Entry, Double>();
	    }

	    public double get(Entry e) {
	        if (components.containsKey(e)) {
	            return components.get(e);
	        } else {
	            return -1;
	        }
	    }

	    public void add(Entry key, Double d) {
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
