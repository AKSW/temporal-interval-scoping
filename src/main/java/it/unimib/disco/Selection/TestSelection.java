package it.unimib.disco.Selection;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import it.unimib.disco.Reasoning.Interval;

public class TestSelection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Interval interval1 = new Interval();
		Interval interval2 = new Interval();
		Interval interval3 = new Interval();
		Interval interval4 = new Interval();
		
		interval1.addStart("2003");
		interval1.addEnd("2005");
		interval1.addValue("0.45");
		
		
		interval2.addStart("2000");
		interval2.addEnd("2001");
		interval2.addValue("0.53");
	
		
		interval3.addStart("2002");
		interval3.addEnd("2006");
		interval3.addValue("0.599");
		
		interval4.addStart("1990");
		interval4.addEnd("2000");
		interval4.addValue("0.6");
		
		List<Interval> ls = new ArrayList<Interval>();
		ls.add(interval1);
		ls.add(interval2);
		ls.add(interval3);
		ls.add(interval4);
		HashMap<String, List<Interval>> obj1= new HashMap<String, List<Interval>>();
		
		obj1.put("barcelona", ls);
		
		System.out.println(new Selection().selection(1, 0, 2, obj1));
	}

}
