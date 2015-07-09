package it.unimib.disco.Evaluation;

import it.unimib.disco.Reasoning.Interval;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TestEvaluation {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		HashMap<String, HashSet<Interval>> timeintervals= new HashMap<String, HashSet<Interval>>();
		HashSet<Interval> ls = new HashSet<Interval>();
		HashSet<Interval> ls2 = new HashSet<Interval>();
		
		List<String> goldstandard_facts = new ArrayList<String>();
		
		Interval interval1 = new Interval();
		Interval interval2 = new Interval();
		Interval interval3 = new Interval();
		
		interval1.addStart("2012");
		interval1.addEnd("2013");
		interval1.addValue("0.45");
		
/*		interval2.addStart("1995");
		interval2.addEnd("2000");
		interval2.addValue("0.53");*/

		ls.add(interval1);
		//ls.add(interval2);
		
		
		interval3.addStart("2003");
		interval3.addEnd("2005");
		interval3.addValue("0.53");
	
		ls2.add(interval3);
		
		 Evaluation_NoReasoning ev = new Evaluation_NoReasoning();
		 //Evaluation ev = new Evaluation();
		String f1="messi	play	barcelona	2012	2013	false";
		String f2="messi	play	barcelona_b	2008	2008	false";
		 
		
		goldstandard_facts.add(f1);
		goldstandard_facts.add(f2);
		 
		FileOutputStream fos1 = new FileOutputStream("eval.csv");
		PrintWriter pw1 = new PrintWriter(fos1);
		timeintervals.put("barcelona", ls);
		timeintervals.put("barcelona_b", ls2);
		
		
		List<QualityMeasure> str = ev.overlap("messi",timeintervals,goldstandard_facts,pw1);
		for(QualityMeasure loc:str){
			System.out.println(loc);
		}
		
	}

}
