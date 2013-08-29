package it.unimib.disco.Reasoning;

import java.util.ArrayList;
import java.util.HashSet;

public class Interval {

	ArrayList<String> interval;
	
	public Interval(){
		interval = new ArrayList(); 
	}
	
	public String getStart (){
		return interval.get(0);
	}
	
	public String getEnd (){
		return interval.get(1);
	}
	
	public void addStart (String start){
		interval.add(0,start);
	}
	public void addEnd (String end){
		interval.add(1,end);
	}
	
	 public String toString()
	    {
	        return interval.toString();
	    }
	 public HashSet<Interval> copyHashSetToHashSet(HashSet<Interval> intervals)
	 {
		 HashSet<Interval> copy = new HashSet<Interval>();
		 for(Interval interval: intervals){
			 copy.add(interval);
		 }
	        return copy;
	 }
	 
	 public HashSet<Interval> copyArrayListToHashSet(ArrayList<Interval> intervals)
	 {
		 HashSet<Interval> copy = new HashSet<Interval>();
		 for(Interval interval: intervals){
			 copy.add(interval);
		 }
	        return copy;
	 }
}
