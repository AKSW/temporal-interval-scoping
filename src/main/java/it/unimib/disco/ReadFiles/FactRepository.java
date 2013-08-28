/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unimib.disco.ReadFiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author rula
 */
public class FactRepository {

    Map<Entry, HashSet<PredicateObject>> components;

    public enum Entry {SUBJECT, OBJECT, PREDICATE, YAGOSTART, YAGOEND, DATE, SCORE};
    public FactRepository() {
        components = new HashMap<Entry, HashSet<PredicateObject>>();
    }

    public HashSet<PredicateObject> get(Entry e) {
        if (components.containsKey(e)) {
            return components.get(e);
        } else {
            return null;
        }
    }

    public void add(Entry key, HashSet<PredicateObject> po) {
        components.put(key, po);
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

