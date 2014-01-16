/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.distributions.Fact.Entry;

/**
 *
 * @author ngonga
 */
public class LocalNormalization implements Normalization {

    public List<Fact> normalize(List<Fact> facts) {
        List<Fact> copy = new ArrayList<Fact>();
        for (int i = 0; i < facts.size(); i++) {
            copy.add(facts.get(i).copy());
        }

        //first get all combinations of URIs and dates
        Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
        for (Fact f : copy) {
            String uri = f.get(Entry.SUBJECT);
            String object = f.get(Entry.OBJECT);

            if (!dates.containsKey(uri)) {
                dates.put(uri, new HashSet<String>());
            }
            if (!dates.get(uri).contains(object)) {
                dates.get(uri).add(object);
            }
        }

        //update the values
        double total;
        for (String subject : dates.keySet()) {
            for (String object : dates.get(subject)) {
                total = 0d;
                for (Fact f : copy) {
                    if (f.get(Entry.SUBJECT).equals(subject) && f.get(Entry.OBJECT).equals(object)) {
                        total = total + Double.parseDouble(f.get(Entry.SCORE));
                    }
                }
                for (Fact f : copy) {
                    if (f.get(Entry.SUBJECT).equals(subject) && f.get(Entry.OBJECT).equals(object)) {
                        f.add(Entry.SCORE, (Double.parseDouble(f.get(Entry.SCORE)) / total) + "");
                    }
                }
            }
        }
        return copy;
    }
}