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
public class Test2 {
    public static void test()
    {
        Fact f1 = new Fact();
        f1.add(Entry.DATE, "2009");
        f1.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f1.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f1.add(Entry.PREDICATE, "p1");
        f1.add(Entry.SCORE, "9");
        
        Fact f2 = new Fact();
        f2.add(Entry.DATE, "2016");
        f2.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f2.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f2.add(Entry.PREDICATE, "p1");
        f2.add(Entry.SCORE, "1");
        
        Fact f3 = new Fact();
        f3.add(Entry.DATE, "1995");
        f3.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f3.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f3.add(Entry.PREDICATE, "p1");
        f3.add(Entry.SCORE, "1");
        
        Fact f4 = new Fact();
        f4.add(Entry.DATE, "2004");
        f4.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f4.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f4.add(Entry.PREDICATE, "p2");
        f4.add(Entry.SCORE, "3");
        
        Fact f5 = new Fact();
        f5.add(Entry.DATE, "2013");
        f5.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f5.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f5.add(Entry.PREDICATE, "p2");
        f5.add(Entry.SCORE, "56");
        
        Fact f6 = new Fact();
        f6.add(Entry.DATE, "2011");
        f6.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f6.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f6.add(Entry.PREDICATE, "p1");
        f6.add(Entry.SCORE, "33");
        
        Fact f7 = new Fact();
        f7.add(Entry.DATE, "2003");
        f7.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f7.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f7.add(Entry.PREDICATE, "p1");
        f7.add(Entry.SCORE, "2");
        
        Fact f8 = new Fact();
        f8.add(Entry.DATE, "2018");
        f8.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f8.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f8.add(Entry.PREDICATE, "p1");
        f8.add(Entry.SCORE, "4");
        
        Fact f9 = new Fact();
        f9.add(Entry.DATE, "2010");
        f9.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f9.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f9.add(Entry.PREDICATE, "p1");
        f9.add(Entry.SCORE, "10");
        
        Fact f10 = new Fact();
        f10.add(Entry.DATE, "2001");
        f10.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f10.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f10.add(Entry.PREDICATE, "p1");
        f10.add(Entry.SCORE, "1");
        
        Fact f11 = new Fact();
        f11.add(Entry.DATE, "1987");
        f11.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f11.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f11.add(Entry.PREDICATE, "p2");
        f11.add(Entry.SCORE, "3");
        
        Fact f12 = new Fact();
        f12.add(Entry.DATE, "2012");
        f12.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f12.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f12.add(Entry.PREDICATE, "p2");
        f12.add(Entry.SCORE, "122");
        
        Fact f13 = new Fact();
        f13.add(Entry.DATE, "2005");
        f13.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        f13.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        f13.add(Entry.PREDICATE, "p1");
        f13.add(Entry.SCORE, "4");
        
        Fact g1 = new Fact();
        g1.add(Entry.DATE, "1995");
        g1.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        g1.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        g1.add(Entry.PREDICATE, "p1");
        g1.add(Entry.SCORE, "3");
        
        Fact g2 = new Fact();
        g2.add(Entry.DATE, "2005");
        g2.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        g2.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        g2.add(Entry.PREDICATE, "p1");
        g2.add(Entry.SCORE, "1");
        
        Fact g3 = new Fact();
        g3.add(Entry.DATE, "2000");
        g3.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        g3.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        g3.add(Entry.PREDICATE, "p1");
        g3.add(Entry.SCORE, "2");
        
        Fact g4 = new Fact();
        g4.add(Entry.DATE, "2012");
        g4.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        g4.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        g4.add(Entry.PREDICATE, "p1");
        g4.add(Entry.SCORE, "2");
        
        Fact g5 = new Fact();
        g5.add(Entry.DATE, "2013");
        g5.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        g5.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        g5.add(Entry.PREDICATE, "p2");
        g5.add(Entry.SCORE, "2");
        
        Fact h1 = new Fact();
        h1.add(Entry.DATE, "2003");
        h1.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        h1.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        h1.add(Entry.PREDICATE, "p2");
        h1.add(Entry.SCORE, "1");
        
        Fact h2 = new Fact();
        h2.add(Entry.DATE, "2004");
        h2.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        h2.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        h2.add(Entry.PREDICATE, "p1");
        h2.add(Entry.SCORE, "3");
        
        Fact h3 = new Fact();
        h3.add(Entry.DATE, "2005");
        h3.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        h3.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        h3.add(Entry.PREDICATE, "p1");
        h3.add(Entry.SCORE, "1");
        
        Fact k1 = new Fact();
        k1.add(Entry.DATE, "2000");
        k1.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k1.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k1.add(Entry.PREDICATE, "p1");
        k1.add(Entry.SCORE, "1");
        
        Fact k2 = new Fact();
        k2.add(Entry.DATE, "2003");
        k2.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k2.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k2.add(Entry.PREDICATE, "p1");
        k2.add(Entry.SCORE, "2");
        
        Fact k3 = new Fact();
        k3.add(Entry.DATE, "1995");
        k3.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k3.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k3.add(Entry.PREDICATE, "p1");
        k3.add(Entry.SCORE, "1");
        
        Fact k4 = new Fact();
        k4.add(Entry.DATE, "2012");
        k4.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k4.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k4.add(Entry.PREDICATE, "p2");
        k4.add(Entry.SCORE, "2");
        
        Fact k5 = new Fact();
        k5.add(Entry.DATE, "2005");
        k5.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k5.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k5.add(Entry.PREDICATE, "p2");
        k5.add(Entry.SCORE, "1");
        
        Fact k6 = new Fact();
        k6.add(Entry.DATE, "2004");
        k6.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        k6.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        k6.add(Entry.PREDICATE, "p1");
        k6.add(Entry.SCORE, "6");
        
        Fact l1 = new Fact();
        l1.add(Entry.DATE, "2009");
        l1.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        l1.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        l1.add(Entry.PREDICATE, "p2");
        l1.add(Entry.SCORE, "1");
        
        Fact l2 = new Fact();
        l2.add(Entry.DATE, "2012");
        l2.add(Entry.SUBJECT, "http://dbpedia.org/resource/Lionel_Messi");
        l2.add(Entry.OBJECT, "http://dbpedia.org/resource/FC_Barcelona");
        l2.add(Entry.PREDICATE, "p2");
        l2.add(Entry.SCORE, "1");
        
        List<Fact> facts = new ArrayList<Fact>();
        facts.add(f1);
        facts.add(f2);
        facts.add(f3);
        facts.add(f4);
        facts.add(f5);
        facts.add(f6);
        facts.add(f7);
        facts.add(f8);
        facts.add(f9);
        facts.add(f10);
        facts.add(f11);
        facts.add(f12);
        facts.add(f13);
        
        facts = (new LocalNormalization()).normalize(facts);
       System.out.println(facts);
        
      /*  List<Fact> allFacts = new ArrayList<Fact>();
        allFacts.add(f1);
        allFacts.add(f2);
        allFacts.add(f3);
        allFacts.add(f4);
        allFacts.add(g1);
        allFacts.add(g2);
        allFacts.add(g3);
        
        allFacts = (new GlobalNormalization()).normalize(allFacts);*/
        //System.out.println(allFacts);
        
    }
    
    public static void main(String args[])
    {
        test();
    }
}
