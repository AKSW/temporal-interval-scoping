package it.unimib.disco.Evaluation;

import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.TemporalInterval;
import it.unimib.disco.TemporalIntervalCreator.MatrixPruningCreator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Evaluation {

	
	public HashMap<String,ArrayList<Double>> overlap(String Uri,HashMap<String,HashSet<ArrayList<String>>> tempodefactoIntervals, List<String> yagoFacts, PrintWriter pw){

		
		HashMap<String,ArrayList<Double>> metricsObj = new HashMap<String,ArrayList<Double>>();
		
		HashMap<String,HashMap<String,ArrayList<String>>> yagoIntervalsUri = groupByEntity(yagoFacts);
		HashMap<String,ArrayList<String>> yagoIntervals=yagoIntervalsUri.get(Uri);
		
		int relevantIntervals=yagoIntervals.size();

		
		double overlapPrec=0,overlapRecall=0,overlapFM=0;

		
		for (String obj: tempodefactoIntervals.keySet()){
			
			HashSet<ArrayList<String>> intervalsRedu = tempodefactoIntervals.get(obj);

			ArrayList<String> yagoInterval = yagoIntervals.get(obj);
			String yagoStart = yagoInterval.get(0);
			String yagoEnd = yagoInterval.get(1);
			
			MatrixPruningCreator mpc= new MatrixPruningCreator();
			double countTcorrect =0.0,countTincorrect =0.0;
			long yearDistanceYago = 0,yearDistance,yearDistanceTot=0;
			
			
			
			for(ArrayList<String> interv:intervalsRedu){
				if(interv.size()!=0){
				Date column = mpc.stringToLong(interv.get(1));					
				Date row = mpc.stringToLong(interv.get(0));

				long millisecDistance = column.getTime()-row.getTime();
				long dayDistance= millisecDistance/(1000 * 60 * 60 * 24);
				yearDistance= dayDistance/365;
				yearDistance=yearDistance+1;
				yearDistanceTot=yearDistanceTot+yearDistance;

				
				if (yagoStart.contains("null")){
					relevantIntervals=relevantIntervals-1;
				}
				else{
					if(yagoEnd.contains("null")){
						yagoEnd="2013";
						
						Date yagoStartDt= mpc.stringToLong(yagoStart);
						Date yagoEndDt=mpc.stringToLong(yagoEnd);
						long millisecDistanceYago = yagoEndDt.getTime()-yagoStartDt.getTime();
						long dayDistanceYago= millisecDistanceYago/(1000 * 60 * 60 * 24);
						yearDistanceYago= dayDistanceYago/365;
						yearDistanceYago=yearDistanceYago+1;

					
						TemporalInterval int1 = new TemporalInterval(row,column);
						TemporalInterval int2 = new TemporalInterval(yagoStartDt,yagoEndDt);
						
						if (int1.during(int2)||int1.starts(int2)||int1.equals(int2)||int1.finishes(int2)){
							countTcorrect= countTcorrect+(double)yearDistance;
							countTincorrect= countTincorrect+((double)yearDistance-(double)yearDistanceYago);
						}
						else if (int1.duringInverse(int2)||int1.startsInverse(int2)||int1.finishesInverse(int2)){
							countTcorrect= countTcorrect+(double)yearDistanceYago;
							countTincorrect= countTincorrect+((double)yearDistanceYago-(double)yearDistance);
						}
						else if (int1.precedes(int2)||int1.follows(int2)){
							countTcorrect= 0.0;
							countTincorrect= countTincorrect-(double)yearDistance;
						}
						else if (int1.meets(int2)||int1.meetsInverse(int2)){
							countTcorrect= countTcorrect+1;
							countTincorrect= countTincorrect-((double)yearDistance+1);
						}
						else{
							Calendar cal = Calendar.getInstance();
							if(int1.overlaps(int2)){
								while (row.before(yagoEndDt)||row.equals(yagoEndDt)){
									Date i = row;
									if((i.after(yagoStartDt)||i.equals(yagoStartDt))&&(i.before(yagoEndDt))){
										countTcorrect= countTcorrect+1;
									}
									else{
										countTincorrect= countTincorrect-1;
									}
									
									cal.setTime(row);
									cal.add(Calendar.YEAR, 1);
									row = cal.getTime();

								}
							}
							else if (int1.overlapsInverse(int2)){
								while (yagoStartDt.before(column)||yagoStartDt.equals(column)){
									Date i = yagoStartDt;
									if((i.after(row)||i.equals(row))&&(i.before(yagoEndDt)||i.equals(yagoEndDt))){
										countTcorrect= countTcorrect+1;
									}
									else{
										countTincorrect= countTincorrect-1;
									}
									cal.setTime(yagoStartDt);
									cal.add(Calendar.YEAR, 1);
									yagoStartDt = cal.getTime();
								}
							}
						}
						//System.out.println(Uri+" "+obj+" "+interv+" "+yearDistance+" "+yearDistanceYago+" "+overlap);

					}
					else{
						Date yagoStartDt= mpc.stringToLong(yagoStart);
						Date yagoEndDt=mpc.stringToLong(yagoEnd);
						long millisecDistanceYago = yagoEndDt.getTime()-yagoStartDt.getTime();
						long dayDistanceYago= millisecDistanceYago/(1000 * 60 * 60 * 24);
						yearDistanceYago= dayDistanceYago/365;
						yearDistanceYago=yearDistanceYago+1;

					
						TemporalInterval int1 = new TemporalInterval(row,column);
						TemporalInterval int2 = new TemporalInterval(yagoStartDt,yagoEndDt);
						
						if (int1.during(int2)||int1.starts(int2)||int1.equals(int2)||int1.finishes(int2)){
							countTcorrect= countTcorrect+(double)yearDistance;
							countTincorrect= countTincorrect+((double)yearDistance-(double)yearDistanceYago);
						}
						else if (int1.duringInverse(int2)||int1.startsInverse(int2)||int1.finishesInverse(int2)){
							countTcorrect= countTcorrect+(double)yearDistanceYago;
							countTincorrect= countTincorrect+((double)yearDistanceYago-(double)yearDistance);
						}
						else if (int1.precedes(int2)||int1.follows(int2)){
							countTcorrect= 0.0;
							countTincorrect= countTincorrect-(double)yearDistance;
						}
						else if (int1.meets(int2)||int1.meetsInverse(int2)){
							countTcorrect= countTcorrect+1;
							countTincorrect= countTincorrect-((double)yearDistance+1);
						}
						else{
							Calendar cal = Calendar.getInstance();
							if(int1.overlaps(int2)){
								while (row.before(yagoEndDt)||row.equals(yagoEndDt)){
									Date i = row;
									if((i.after(yagoStartDt)||i.equals(yagoStartDt))&&(i.before(yagoEndDt))){
										countTcorrect= countTcorrect+1;
									}
									else{
										countTincorrect= countTincorrect-1;
									}
									
									cal.setTime(row);
									cal.add(Calendar.YEAR, 1);
									row = cal.getTime();

								}
							}
							else if (int1.overlapsInverse(int2)){
								while (yagoStartDt.before(column)||yagoStartDt.equals(column)){
									Date i = yagoStartDt;
									if((i.after(row)||i.equals(row))&&(i.before(yagoEndDt)||i.equals(yagoEndDt))){
										countTcorrect= countTcorrect+1;
									}
									else{
										countTincorrect= countTincorrect-1;
									}
									cal.setTime(yagoStartDt);
									cal.add(Calendar.YEAR, 1);
									yagoStartDt = cal.getTime();
								}
							}
						}
						//System.out.println(Uri+" "+obj+" "+interv+" "+yearDistance+" "+yearDistanceYago+" "+overlap);
				}
							
			}
			
		}

		}
		
		//values for a single temporal interval
		overlapPrec  = countTcorrect/yearDistanceTot;
		overlapRecall = countTcorrect/yearDistanceYago;
		overlapFM  = 2*(overlapPrec*overlapRecall)/(overlapPrec+overlapRecall);
		
		ArrayList<Double> metrics=new ArrayList<Double>();
		metrics.add(overlapPrec);
		metrics.add(overlapRecall);
		metrics.add(overlapFM);
		
		metricsObj.put(obj,metrics);
		
		/*ArrayList<Double> metrics=new ArrayList<Double>();
		metrics.add(countTcorrect);
		metrics.add((double)yearDistanceTot);
		metrics.add((double)yearDistanceYago);*/

		//pw.println("Subject"+" "+"Object"+" "+"Retrieved Interval"+" "+"Yago Interval"+" "+"Precision"+" "+"Recall"+" "+"F1");
		pw.println(Uri+" "+obj+" "+intervalsRedu+" "+yagoInterval+" "+overlapPrec+" "+overlapRecall+" "+overlapFM);
		//pw.println(Uri+" "+obj+" "+intervalsRedu+" "+yagoInterval+" "+countTcorrect+" "+yearDistanceTot+" "+yearDistanceYago);
		
		}
		
		return metricsObj;
	}
	
	public HashMap<String,ArrayList<Double>> precisionRecall(String Uri,HashMap<String,HashSet<ArrayList<String>>> tempodefactoIntervals, List<String> yagoFacts){

		HashMap<String,ArrayList<Double>> metricsUri = new HashMap<String,ArrayList<Double>>();
		HashMap<String,HashMap<String,ArrayList<String>>> yagoIntervalsUri = groupByEntity(yagoFacts);
		HashMap<String,ArrayList<String>> yagoIntervals=yagoIntervalsUri.get(Uri);
		int relevantIntervals=yagoIntervals.size();
		System.out.println("relevant intervals " + yagoIntervals);
		
		double localPrecision=0.0,localRecall=0.0;
		int relevAndRetriev = 0,retrievedIntervals=0;
		

		for (String obj: tempodefactoIntervals.keySet()){
			HashSet<ArrayList<String>> intervalsRedu = tempodefactoIntervals.get(obj);
			retrievedIntervals+=intervalsRedu.size();
			System.out.println(Uri+" " + obj+ " intervals selected "+ intervalsRedu);
			ArrayList<String> yagoInterval = yagoIntervals.get(obj);
			
			MatrixPruningCreator mpc= new MatrixPruningCreator();
			
			for(ArrayList<String> interv:intervalsRedu){
				Date column = mpc.stringToLong(interv.get(1));					
				Date row = mpc.stringToLong(interv.get(0));
				
				String yagoStart = yagoInterval.get(0);
				String yagoEnd = yagoInterval.get(1);
				
				if (yagoStart.contains("null")){
					relevantIntervals=relevantIntervals-1;
				}
				else{
					if(yagoEnd.contains("null")){
						yagoEnd="2013-12-31";
						
						if((row.equals(mpc.stringToLong(yagoStart)))&&(column.equals(mpc.stringToLong(yagoEnd)))){
							
							System.out.println(obj +" correct intervals " + interv);
							
							relevAndRetriev++;
						}
					}
					else{

						if((row.equals(mpc.stringToLong(yagoStart)))&&(column.equals(mpc.stringToLong(yagoEnd)))){
							System.out.println(obj +" correct intervals " + interv);
							relevAndRetriev++;
						}
					}
				}
			}
		}
		System.out.println("Ret: "+retrievedIntervals+" C: "+relevAndRetriev+" Rel"+relevantIntervals);
		localPrecision = relevAndRetriev*1.0/retrievedIntervals;
		localRecall = relevAndRetriev*1.0/relevantIntervals;
		
		ArrayList<Double> metrics=new ArrayList<Double>();
		metrics.add(localPrecision);
		metrics.add(localRecall);
		metricsUri.put(Uri, metrics);
		
		return metricsUri;
	}
	
	public HashMap<String,HashMap<String,ArrayList<String>>> groupByEntity(List<String> temporaldefacto){
		ReadFiles rf=new ReadFiles();
		HashSet<ArrayList<String>> file=rf.readTabSeparatedFile(temporaldefacto);
		
		HashMap<String,HashMap<String,ArrayList<String>>> resource = new HashMap<String,HashMap<String,ArrayList<String>>>();
		HashMap<String,ArrayList<String>> yagoIntervals= new HashMap<String,ArrayList<String>>(); 
		
		for (ArrayList<String> record:file){
			ArrayList<String> yagoInterval = new ArrayList<String>();
			if (!resource.containsKey(record.get(0))){
				yagoIntervals = new HashMap<String,ArrayList<String>>();
				resource.put(record.get(0), yagoIntervals);
			}
			if(yagoIntervals.containsKey(record.get(2))){
				yagoInterval = new ArrayList<String>();
				yagoIntervals.put(record.get(2), yagoInterval );
			} 
			
			yagoIntervals=resource.get(record.get(0));
			yagoInterval.add(record.get(3));
			yagoInterval.add(record.get(4));
			yagoIntervals.put(record.get(2),yagoInterval);
			
		resource.put(record.get(0), yagoIntervals);	
		}
		
		
		/*for ( String str: resource.keySet()){
			HashMap<String,ArrayList<String>> hm = resource.get(str);
			for (String y: hm.keySet()){
				System.out.println(str+" "+ y+" "+ hm.get(y));
			}
		}*/
		return resource;
	}
	

}