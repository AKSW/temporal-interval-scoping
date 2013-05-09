package it.unimib.disco.Reasoning;

import it.unimib.disco.TemporalIntervalCreator.MatrixPruningCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Reasoning {

	public HashSet<ArrayList<String>> concatenate2Intervlas(ArrayList<String> interval1, ArrayList<String> interval2){
		
		HashSet<ArrayList<String>> intervResult= new HashSet<ArrayList<String>>();
		ArrayList<String> interval= new ArrayList<String>();
	
		MatrixPruningCreator mpc= new MatrixPruningCreator();
			String startDateStr1=interval1.get(0);
			String	endDateStr1=interval1.get(1);
			String	startDateStr2=interval2.get(0);
			String	endDateStr2=interval2.get(1);

			Date startDate1 = mpc.stringToLong(startDateStr1);
			Date endDate1 = mpc.stringToLong(endDateStr1);
			TemporalInterval int1 = new TemporalInterval(startDate1,endDate1);
			
			Date startDate2 = mpc.stringToLong(startDateStr2);
			Date endDate2 = mpc.stringToLong(endDateStr2);
			TemporalInterval int2 = new TemporalInterval(startDate2,endDate2);
			
			if (int1.during(int2)){
				interval.add(startDateStr2);
				interval.add(endDateStr2);
			}
			else if (int1.duringInverse(int2)){
				interval.add(startDateStr1);
				interval.add(endDateStr1);
			}
			else if (int1.overlaps(int2)||int1.meets(int2)||int1.starts(int2)||int1.finishesInverse(int2)){
				interval.add(startDateStr1);
				interval.add(endDateStr2);
			}
			else if (int1.overlapsInverse(int2)||int1.meetsInverse(int2)||int1.startsInverse(int2)||int1.finishes(int2)){
				interval.add(startDateStr2);
				interval.add(endDateStr1);
			}
			
			else{
				
				intervResult.add(interval1);
				intervResult.add(interval2);

			}
			intervResult.add(interval);
		
		return intervResult;
		
		
	}
	
	public HashSet<ArrayList<String>> concatenateIntervlas (HashSet<ArrayList<String>> intervals){
		HashSet<ArrayList<String>> interv = new HashSet<ArrayList<String>>();

		if (intervals.size()>1){
		
			int i=0;
			for (ArrayList<String> interval1:intervals){
				int j=0;
				for(ArrayList<String> interval2:intervals){
					if(j>i){
						
						HashSet<ArrayList<String>> intervTemp=concatenate2Intervlas(interval1,interval2);
						interv.addAll(intervTemp);
					}
					j++;
				}
				i++;
			}

		}
		else{

			for (ArrayList<String> interval:intervals){
				interv.add(interval);
			}

		}
		return interv;
	}
}
