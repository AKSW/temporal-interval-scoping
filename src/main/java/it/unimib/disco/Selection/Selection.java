package it.unimib.disco.Selection;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;
/**
*
* @author rula
*/

public class Selection {

	public HashMap<String,HashSet<ArrayList<String>>> selection(int selection, int x, int k, HashMap<String,HashSet<ArrayList<String>>> objIntervals){
		HashMap<String,HashSet<ArrayList<String>>> objIntervalsRedu = new HashMap<String,HashSet<ArrayList<String>>>();
		HashSet<ArrayList<String>> sel = new HashSet<ArrayList<String>>();
		
		for (String obj: objIntervals.keySet()){
			
			HashSet<ArrayList<String>> temporalDCobjIntervals= objIntervals.get(obj);
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
	

	public HashSet<ArrayList<String>> combinedProxyTopk (HashSet<ArrayList<String>> intervals, int x,int k) throws ParseException{
			

		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();
		
			ArrayList<String> interMax=new ArrayList<String>();
			double value=0,max=0;
			for (ArrayList<String> interv: intervals){
			
				if (interv.size() != 0){
				
					value=Double.valueOf((interv.get(2).trim()));
					if(value > max && value!=0.0){
					
						max=value;

					}
				}	
				
			}
			
			for (ArrayList<String> interv: intervals){
				
				if (interv.size() != 0){
				
					value=Double.valueOf((interv.get(2).trim()));

					if(value >= max && value!=0.0){
		
						interMax=new ArrayList<String>();
						interMax.add(interv.get(0));
						interMax.add(interv.get(1));
						interMax.add(interv.get(2));
						
						if (interMax.size()!=0 && k>=1){
							//System.out.println(interMax);
							allIntervals.add(interMax);

							k--;
							max=max-(x*max)/100;

						}
	

					}

				}	

		}

		return allIntervals;
	}
	
	public HashSet<ArrayList<String>> proxSelect (HashSet<ArrayList<String>> intervals, int x) throws ParseException{
			
		
		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();

		double maxProv=0;
		
	
			ArrayList<String> interMax=new ArrayList<String>();
			double value=0,max=0;
			for (ArrayList<String> interv: intervals){
			
				if (interv.size() != 0){
				
					value=Double.valueOf((interv.get(2).trim()));
					if(value > max && value!=0.0){
					
						max=value;

					}
				}	
				
			}
			
			maxProv=max-(x*max)/100;

			for (ArrayList<String> interv: intervals){
				
				if (interv.size() != 0){
				
					value=Double.valueOf((interv.get(2).trim()));

					if(value <= max && value >= maxProv && value!=0.0){
		
						interMax=new ArrayList<String>();
						interMax.add(interv.get(0));
						interMax.add(interv.get(1));
						interMax.add(interv.get(2));
						
						if (interMax.size()!=0){
							//System.out.println(interMax);
							allIntervals.add(interMax);
						}

					}

				}	
				
			}
			
		return allIntervals;
	}
	
	public HashSet<ArrayList<String>> topK (HashSet<ArrayList<String>> intervals, int k) throws ParseException{
			
		
		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();


		double maxProv=5000.00;
		int count=0;
		while (count<k){
			ArrayList<String> interMax=new ArrayList<String>();
			double value=0,max=0;
			for (ArrayList<String> interv: intervals){
			
				if (interv.size() != 0){
				
					value=Double.valueOf((interv.get(2).trim()));
					if(value<maxProv){
					if(value > max && value!=0.0){
					
						max=value;
		
						interMax=new ArrayList<String>();
						interMax.add(interv.get(0));
						interMax.add(interv.get(1));
						interMax.add(interv.get(2));

					}
					}
				}	
				
			}
			
			maxProv=max;
			if (interMax.size()!=0){

				allIntervals.add(interMax);
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
	

	public HashSet<ArrayList<String>> retrieveAbsoluteDeviation (HashSet<ArrayList<String>> intervals) throws ParseException{
		
		double value=0;
		double arrayValues[]= new double[intervals.size()];
		
		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();
		ArrayList<String> interval=new ArrayList<String>();
		int i=0;
		for (ArrayList<String> interv: intervals){
		
			if (interv.size() != 0){
				
				value=Double.valueOf((interv.get(2).trim()));
				arrayValues[i]=value;
				i++;
			}
		}
		/*Variance var = new Variance ();
		double variance = var.evaluate(arrayValues);*/
		
		Mean mean = new Mean();
		double m=mean.evaluate(arrayValues);
		
		for (ArrayList<String> interv: intervals){
			
			if (interv.size() != 0){
				
				value=Double.valueOf((interv.get(2).trim()));
				if (value>m){

					interval=new ArrayList<String>();
					interval.add(interv.get(0));
					interval.add(interv.get(1));
					interval.add(interv.get(2));
					if (interval.size()!=0){
						allIntervals.add(interval);
					}
				}
				
				
			}
		}

		return allIntervals;
	}
	
	public HashSet<ArrayList<String>> retrieveAVGDeviation (HashSet<ArrayList<String>> intervals) throws ParseException{
		
		double value=0;
		double arrayValues[]= new double[intervals.size()];
		
		HashSet<ArrayList<String>> allIntervals= new HashSet<ArrayList<String>>();
		ArrayList<String> interval=new ArrayList<String>();
		int i=0;
		for (ArrayList<String> interv: intervals){
		
			if (interv.size() != 0){
				
				value=Double.valueOf((interv.get(2).trim()));
				arrayValues[i]=value;
				i++;
			}
		}
		Variance var = new Variance ();
		double variance = var.evaluate(arrayValues);
		double sd = Math.sqrt(variance);
		
		Mean mean = new Mean();
		double m=mean.evaluate(arrayValues);
		
		for (ArrayList<String> interv: intervals){
			
			if (interv.size() != 0){
				
				value=Double.valueOf((interv.get(2).trim()));
				if (value<=(m+sd)||value >=(m-sd)){

					interval=new ArrayList<String>();
					interval.add(interv.get(0));
					interval.add(interv.get(1));
					interval.add(interv.get(2));
					if (interval.size()!=0){
						allIntervals.add(interval);
					}
				}
				
				
			}
		}

		return allIntervals;
	}

}
