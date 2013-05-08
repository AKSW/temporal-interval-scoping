/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ngonga
 */
public class Fact {

    Map<Entry, String> components;

    public enum Entry {SUBJECT, OBJECT, PREDICATE, DATE, SCORE};
    public Fact() {
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

    public Fact copy() {
        Fact f = new Fact();
        for (Entry key : components.keySet()) {
            f.add(key, components.get(key));
        }
        return f;
    }
    
    public String toString()
    {
        return components.toString();
    }
}

