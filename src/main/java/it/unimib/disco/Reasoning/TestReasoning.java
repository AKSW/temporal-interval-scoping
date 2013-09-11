package it.unimib.disco.Reasoning;

import java.util.ArrayList;
import java.util.HashSet;

public class TestReasoning {

	public static void main(String[] args) {
		ArrayList<Interval> intervals = new ArrayList<Interval>(); 
		Interval interval1 = new Interval();
		Interval interval2 = new Interval();
		Interval interval3 = new Interval();
		Interval interval4 = new Interval();
		
		interval1.addStart("2003");
		interval1.addEnd("2005");
		
		
		interval2.addStart("2000");
		interval2.addEnd("2001");
	
		
		interval3.addStart("2002");
		interval3.addEnd("2006");
		
		interval4.addStart("1990");
		interval4.addEnd("2000");
		
		intervals.add(interval1);
		intervals.add(interval2);
		intervals.add(interval3);
		intervals.add(interval4);

		HashSet<Interval> result = new Reasoning().concatenateIntervals(intervals);
		System.out.println(result);
		
	}

}
