package it.unimib.disco.Selection;
import it.unimib.disco.Reasoning.Interval;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/**
*
* @author rula
*/

public class Selection {

	public HashMap<String,List<Interval>> selection(int selection, int x, int k, HashMap<String,List<Interval>> objIntervals){
		HashMap<String,List<Interval>> objIntervalsRedu = new HashMap<String,List<Interval>>();
		List<Interval> sel = new ArrayList<Interval>();
		
		for (String obj: objIntervals.keySet()){
		
			List<Interval> temporalDCobjIntervals= objIntervals.get(obj);
			try {
				
				if(selection==1){
					sel=topK(temporalDCobjIntervals,k);

				}
				else if(selection==2){
					sel=proxSelect(temporalDCobjIntervals, x);
				}
				else if(selection==3){
					sel=combinedProxyTopk(temporalDCobjIntervals,x,k);
				}
				

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(sel.size()!=0){

				objIntervalsRedu.put(obj, sel);

			}
		}
		return objIntervalsRedu;
	}
	

	public List<Interval> combinedProxyTopk(List<Interval> intervals, int x,int k) throws ParseException{
			
		List<Interval> allIntervals= new ArrayList<Interval>();
		
			
			double maxProv=0d,value=0d,max=0d;
			int count=0;
			for (Interval interv: intervals){
					value=Double.valueOf((interv.getValue().trim()));
					if(value >= max && value!=0.0){
					
						max=value;

					}
			}
			if(x==0){
				maxProv=0d;
			}
			else{
				maxProv=max-(x*max)/100;
			}
			while (count<k){
				Interval interMax=new Interval();
				boolean added=false;
				int index=0;
				value=0d;max=0d;
				for (int i=0;i<intervals.size();i++){
						
					value=Double.valueOf((intervals.get(i).getValue().trim()));
					if(value>=maxProv){
						if(value >= max && value!=0){
						
							max=value;
			
							interMax=new Interval();
							interMax.addStart(intervals.get(i).getStart());
							interMax.addEnd(intervals.get(i).getEnd());
							interMax.addValue(intervals.get(i).getValue());
							index=i;
							added=true;
						}
						
					}	
					
				}
				
				if(added){			
					allIntervals.add(interMax);
					intervals.remove(index);
				}
				count++;
			}

		return allIntervals;
	}
	
	public List<Interval> combinedProxyTopk_v1 (List<Interval> intervals, int x,int k) {
                        
		List<Interval> allIntervals= new ArrayList<Interval>();
	
			double maxProv=0d,value=0d,max=0d;
			int count=0;
			for (Interval interv: intervals){
					value=Double.valueOf((interv.getValue().trim()));
					if(value >= max && value!=0.0){
					
						max=value;

					}
			}
			if(x==0){
				maxProv=0d;
			}
			else{
				maxProv=max-(x*max)/100;
				
			}
			while (count<k){
				Interval interMax=new Interval();
				boolean added=false;
				int index=0;
				value=0d;double maxP=max;
				for (int i=0;i<intervals.size();i++){
					
					value=Double.valueOf((intervals.get(i).getValue().trim()));
					if(value>=maxProv){

						if(value <= maxP && value!=0){
						
							maxP=value;
							
							interMax=new Interval();
							interMax.addStart(intervals.get(i).getStart());
							interMax.addEnd(intervals.get(i).getEnd());
							interMax.addValue(intervals.get(i).getValue());
							index=i;
							added=true;
						}
					}	
					
				}
				
				if(added){			
					allIntervals.add(interMax);
					intervals.remove(index);
				}
				count++;
			}

		return allIntervals;
	}
	
	public List<Interval> proxSelect (List<Interval> intervals, int x) throws ParseException{		
		
		List<Interval> allIntervals= new ArrayList<Interval>();

		double maxProv=0;
		
			Interval interMax=new Interval();
			double value=0,max=0;
			for (Interval interv: intervals){
	
					value=Double.valueOf((interv.getValue().trim()));
					if(value >= max && value!=0.0){
					
						max=value;

					}
			}
			
			maxProv=max-(x*max)/100;

			for (Interval interv: intervals){

					value=Double.valueOf((interv.getValue().trim()));

					if(value <= max && value >= maxProv && value!=0.0){
		
						interMax=new Interval();
						interMax.addStart(interv.getStart());
						interMax.addEnd(interv.getEnd());
						interMax.addValue(interv.getValue());
						
						allIntervals.add(interMax);

				}	
				
			}
			
		return allIntervals;
	}
	
	public List<Interval> topK (List<Interval> intervals, int k) throws ParseException{
		//List<Interval> intervals=new Interval().sort(list);

		List<Interval> allIntervals= new ArrayList<Interval>();
	
		int count=0;
		while (count<k){
			boolean added=false;
			Interval interMax=new Interval();
			int index=0;
			double value=0,max=0;
			for (int i=0;i<intervals.size();i++){
					
					value=Double.valueOf((intervals.get(i).getValue().trim()));
					
					if(value >= max && value!=0){
					
						max=value;
		
						interMax=new Interval();
						interMax.addStart(intervals.get(i).getStart());
						interMax.addEnd(intervals.get(i).getEnd());
						interMax.addValue(intervals.get(i).getValue());
						index=i;
						added=true;
					}

				
			}
			
			if(added){			
			allIntervals.add(interMax);
			intervals.remove(index);
			}
			count++;
		}
	
		return allIntervals;
	}
	
	public HashSet<ArrayList<String>> retrieveMax (HashSet<ArrayList<String>> intervals) throws ParseException{
		
		double value=0,max=0;
		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();
		ArrayList<String> interMax=new ArrayList<String>();
		
		for (ArrayList<String> interv: intervals){
		
			if (interv.size() != 0){
			
				value=Double.valueOf((interv.get(2).trim()));
			
				if(value > max&& value!=0.0){
				
					max=value;

					interMax=new ArrayList<String>();
					interMax.add(interv.get(0));
					interMax.add(interv.get(1));
					interMax.add(interv.get(2));
				}
			}	
			
		}
		if (interMax.size()!=0){
			allIntervals.add(interMax);
		}
		return allIntervals;
	}
	

	
	
	

}
