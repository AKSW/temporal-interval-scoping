package it.unimib.disco.Reasoning;

import it.unimib.disco.TemporalIntervalCreator.MatrixCreator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Reasoning {
	
	public HashSet<Interval> concatenate2Intervals(Interval interval1, Interval interval2){
		
		HashSet<Interval> intervResult= new HashSet<Interval>();
		Interval interval= new Interval();
	
		MatrixCreator mpc= new MatrixCreator();
			String startDateStr1=interval1.getStart();
			String	endDateStr1=interval1.getEnd();
			String	startDateStr2=interval2.getStart();
			String	endDateStr2=interval2.getEnd();

			Date startDate1 = mpc.stringToLong(startDateStr1);
			Date endDate1 = mpc.stringToLong(endDateStr1);
			TemporalInterval int1 = new TemporalInterval(startDate1,endDate1);
			
			Date startDate2 = mpc.stringToLong(startDateStr2);
			Date endDate2 = mpc.stringToLong(endDateStr2);
			TemporalInterval int2 = new TemporalInterval(startDate2,endDate2);
			
			if (int1.during(int2)){
				interval.addStart(startDateStr2);
				interval.addEnd(endDateStr2);
			}
			else if (int1.duringInverse(int2)){
				interval.addStart(startDateStr1);
				interval.addEnd(endDateStr1);
			}
			else if (int1.overlaps(int2)||int1.meets(int2)||int1.starts(int2)||int1.finishesInverse(int2)){
				interval.addStart(startDateStr1);
				interval.addEnd(endDateStr2);
			}
			else if (int1.overlapsInverse(int2)||int1.meetsInverse(int2)||int1.startsInverse(int2)||int1.finishes(int2)){
				interval.addStart(startDateStr2);
				interval.addEnd(endDateStr1);
			}
			
			else if (int1.equals(int2)){
				interval.addStart(startDateStr1);
				interval.addEnd(endDateStr2);

			}
			else if(int1.precedes(int2)||int1.follows(int2)){
				intervResult.add(interval1);
				intervResult.add(interval2);
			}
			if (intervResult.isEmpty()){
				intervResult.add(interval);
			}
		
		return intervResult;
		
		
	}
	
	
	public HashSet<Interval> concatenateIntervals (List<Interval> list){
		HashSet<Interval> result = new HashSet<Interval>();

		List<Interval> intervals=new Interval().sort(list);
		result = new Interval().copyArrayListToHashSet(intervals);

		if (intervals.size()>1){
		
			for (int i=0;i<intervals.size();i++){
				HashSet<Interval> resultTemp= new HashSet<Interval>();
				for(Interval interval2 : result){
					
					HashSet<Interval> intervTemp=concatenate2Intervals(intervals.get(i),interval2);
					for (Interval interv:intervTemp){
						
						resultTemp.add(interv);
					}
					
				}
				
				result = new Interval().unique(resultTemp);
				//System.out.println(result);			
				
				}
		}
		else{
				result.add(intervals.get(0));
			}

		return result;
	
	}
}
