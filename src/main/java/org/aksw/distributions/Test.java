/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.util.ArrayList;
import java.util.List;
import org.aksw.distributions.Fact.Entry;

/**
 *
 * @author ngonga
 */
public class Test {
    public static void test()
    {
        Fact f1 = new Fact();
        f1.add(Entry.DATE, "1999");
        f1.add(Entry.SUBJECT, "a");
        f1.add(Entry.OBJECT, "b");
        f1.add(Entry.PREDICATE, "p1");
        f1.add(Entry.SCORE, "1");
        
        Fact f2 = new Fact();
        f2.add(Entry.DATE, "2000");
        f2.add(Entry.SUBJECT, "a");
        f2.add(Entry.OBJECT, "b");
        f2.add(Entry.PREDICATE, "p1");
        f2.add(Entry.SCORE, "10");
        
        Fact f3 = new Fact();
        f3.add(Entry.DATE, "2011");
        f3.add(Entry.SUBJECT, "a");
        f3.add(Entry.OBJECT, "b");
        f3.add(Entry.PREDICATE, "p1");
        f3.add(Entry.SCORE, "5");
        
        Fact f4 = new Fact();
        f4.add(Entry.DATE, "2011");
        f4.add(Entry.SUBJECT, "a");
        f4.add(Entry.OBJECT, "b");
        f4.add(Entry.PREDICATE, "p2");
        f4.add(Entry.SCORE, "5");
        
        Fact g1 = new Fact();
        g1.add(Entry.DATE, "1999");
        g1.add(Entry.SUBJECT, "a");
        g1.add(Entry.OBJECT, "b");
        g1.add(Entry.PREDICATE, "p2");
        g1.add(Entry.SCORE, "1");
        
        Fact g2 = new Fact();
        g2.add(Entry.DATE, "2000");
        g2.add(Entry.SUBJECT, "a");
        g2.add(Entry.OBJECT, "b");
        g2.add(Entry.PREDICATE, "p1");
        g2.add(Entry.SCORE, "3");
        
        Fact g3 = new Fact();
        g3.add(Entry.DATE, "2001");
        g3.add(Entry.SUBJECT, "a");
        g3.add(Entry.OBJECT, "b");
        g3.add(Entry.PREDICATE, "p1");
        g3.add(Entry.SCORE, "10");
        
//        List<Fact> facts = new ArrayList<Fact>();
//        facts.add(f1);
//        facts.add(f2);
//        facts.add(f3);
//        facts.add(f4);
//        
//        facts = (new LocalNormalization()).normalize(facts);
//        System.out.println(facts);
        
        List<Fact> allFacts = new ArrayList<Fact>();
        allFacts.add(f1);
        allFacts.add(f2);
        allFacts.add(f3);
        allFacts.add(f4);
        allFacts.add(g1);
        allFacts.add(g2);
        allFacts.add(g3);
        
        allFacts = (new GlobalNormalization()).normalize(allFacts);
        System.out.println(allFacts);
        
    }
    
    public static void main(String args[])
    {
        test();
    }
}
