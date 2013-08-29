package it.unimib.disco.Reasoning;

import java.util.ArrayList;
import java.util.HashSet;

public class Test4 {

	public static void main(String[] args) {
		ArrayList<Interval> intervals = new ArrayList<Interval>(); 
		Interval interval1 = new Interval();
		Interval interval2 = new Interval();
		Interval interval3 = new Interval();
		
		interval1.addStart("2003");
		interval1.addEnd("2005");
		
		
		interval2.addStart("2000");
		interval2.addEnd("2001");
		
		
		interval3.addStart("2002");
		interval3.addEnd("2006");
		
		intervals.add(interval1);
		intervals.add(interval2);
		intervals.add(interval3);

		HashSet<Interval> result = new Reasoning().concatenateIntervals(intervals);
		for (Interval r:result){
			System.out.println(r);
		}
	}

}
