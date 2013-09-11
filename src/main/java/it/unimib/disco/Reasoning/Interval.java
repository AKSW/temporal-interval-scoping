package it.unimib.disco.Reasoning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Interval {

	ArrayList<String> interval;
	
	public Interval(){
		interval = new ArrayList<String>(); 
	}
	
	public String getStart (){
		return interval.get(0);
	}
	
	public String getEnd (){
		return interval.get(1);
	}
	public String getValue (){
		return interval.get(2);
	}
	
	public void addStart (String start){
		interval.add(0,start);
	}
	public void addEnd (String end){
		interval.add(1,end);
	}
	public void addValue (String value){
		interval.add(2,value);
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
	 
	 public HashSet<Interval> copyArrayListToHashSet(List<Interval> intervals)
	 {
		 HashSet<Interval> copy = new HashSet<Interval>();
		 for(Interval interval: intervals){
			 copy.add(interval);
		 }
	        return copy;
	 }
	 
	 public List<Interval> sort(List<Interval> ar)
	 {
		List<String> startDates= new ArrayList<String>();
		for (int i=0;i<ar.size();i++){
			startDates.add(ar.get(i).getStart());
		}
		Collections.sort(startDates);
		List<Interval> copy = new ArrayList<Interval>();
		for (int i=0;i<startDates.size();i++){
			for(int j=0;j<ar.size();j++){
				if(startDates.get(i).equals(ar.get(j).getStart())){
					if(copy.contains(ar.get(j))){}
					else{
						copy.add(i, ar.get(j));
					}
				}
			}
		}
		return copy;

	 }
	 
	 
	 public HashSet<Interval> unique(HashSet<Interval> intervals)
	 {
		 
		 List<Interval> copylist = new ArrayList<Interval>();
		 copylist.add(intervals.iterator().next());
		 
		 Iterator<Interval> it=intervals.iterator();
		 while(it.hasNext()){
			 Interval interval = (Interval) it.next();
			 int i=0;
			 boolean present = false;
			 while(i<copylist.size()&&!present){
			
				if ((interval.getStart().contains(copylist.get(i).getStart())) && ((interval.getEnd().contains(copylist.get(i).getEnd())))){
					present=true;
				}
				if (i==(copylist.size()-1)&&!present){
					copylist.add(interval);
				}
				i++;
			 }
			

		 }
		 HashSet<Interval> copy = new HashSet<Interval>(copylist);
	     return copy;
	 }
}
