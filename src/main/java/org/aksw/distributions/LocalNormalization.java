/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import com.hp.hpl.jena.sparql.function.library.date;
import java.util.*;
import org.aksw.distributions.Fact.Entry;

/**
 *
 * @author ngonga
 */
public class LocalNormalization implements Normalization {

    public List<Fact> computeSmoothing(List<Fact> facts) {
        List<Fact> copy = new ArrayList<Fact>();
        for (int i = 0; i < facts.size(); i++) {
            copy.add(facts.get(i).copy());
        }

        //first get all combinations of URIs and dates
        Map<String, Set<String>> dates = new HashMap<String, Set<String>>();
        for (Fact f : copy) {
            String uri = f.get(Entry.SUBJECT);
            String predicate = f.get(Entry.PREDICATE);

            if (!dates.containsKey(uri)) {
                dates.put(uri, new HashSet<String>());
            }
            if (!dates.get(uri).contains(predicate)) {
                dates.get(uri).add(predicate);
            }
        }

        //update the values
        double total;
        for (String subject : dates.keySet()) {
            for (String property : dates.get(subject)) {
                total = 0d;
                for (Fact f : copy) {
                    if (f.get(Entry.SUBJECT).equals(subject) && f.get(Entry.PREDICATE).equals(property)) {
                        total = total + Double.parseDouble(f.get(Entry.SCORE));
                    }
                }
                for (Fact f : copy) {
                    if (f.get(Entry.SUBJECT).equals(subject) && f.get(Entry.PREDICATE).equals(property)) {
                        f.add(Entry.SCORE, (Double.parseDouble(f.get(Entry.SCORE)) / total) + "");
                    }
                }
            }
        }
        return copy;
    }
}