/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ngonga
 */
public class FactReader {
    public static Fact readFact(List<String> fact)
    {
        Fact f = new Fact();
        f.add(Fact.Entry.SUBJECT, fact.get(0));
        f.add(Fact.Entry.PREDICATE, fact.get(1));
        f.add(Fact.Entry.OBJECT, fact.get(2));
        f.add(Fact.Entry.YAGOSTART, fact.get(3));
        f.add(Fact.Entry.YAGOEND, fact.get(4));
        f.add(Fact.Entry.DATE, fact.get(5));
        f.add(Fact.Entry.SCORE, fact.get(6));
        return f;
    }
    
    public static List<String> listFromFact(Fact f)
    {
        List<String> l = new ArrayList<String>();
        l.add(f.get(Fact.Entry.SUBJECT));
        l.add(f.get(Fact.Entry.PREDICATE));
        l.add(f.get(Fact.Entry.OBJECT));
        l.add(f.get(Fact.Entry.YAGOSTART));
        l.add(f.get(Fact.Entry.YAGOEND));
        l.add(f.get(Fact.Entry.DATE));
        l.add(f.get(Fact.Entry.SCORE));
        return l;
    }
}
