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
		
		interval1.addStart("2009");
		interval1.addEnd("2013");
		
		
		interval2.addStart("2001");
		interval2.addEnd("2004");
	
		
		interval3.addStart("2003");
		interval3.addEnd("2004");
		
		interval4.addStart("2002");
		interval4.addEnd("2010");
		
		intervals.add(interval1);
		intervals.add(interval2);
		intervals.add(interval3);
		intervals.add(interval4);

		HashSet<Interval> result = new Reasoning().concatenateIntervals(intervals);
		System.out.println(result);
		
	}
//[[2003, 2006], [2009, 2013], [2004, 2006, 0.18333333333333335]]
	//[[2007, 2013], [2009, 2013, 0.1380952380952381], [1986, 1993, 0.11904761904761904]]
	//[[2004, 2005, 0.13793103448275862], [2011, 2013, 0.22413793103448276], [2007, 2013]]
	//[[1986, 1993], [2006, 2013], [2007, 2013], [2009, 2013, 0.1380952380952381]]
	//[[2009, 2013, 0.13541666666666666], [2001, 2004], [2003, 2004, 0.1875], [2002, 2004]]
	//http://dbpedia.org/resource/Arjen_Robben http://dbpedia.org/resource/Chelsea_F.C. [[2003, 2004, 0.1875], [2009, 2013, 0.13541666666666666], [2002, 2004, 0.125], [2001, 2004, 0.125]]	
}
