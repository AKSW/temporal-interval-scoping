package it.unimib.disco.Evaluation;

import it.unimib.disco.Evaluation.QualityMeasure.Entry;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.Reasoning.Interval;
import it.unimib.disco.Reasoning.TemporalInterval;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Evaluation_NoReasoning {

	
	public List<QualityMeasure> overlap(String Uri,HashMap<String,HashSet<Interval>> tempodefactoIntervals, List<String> goldstandard_facts, PrintWriter pw){
	
		List<QualityMeasure> metricsObj = new ArrayList<QualityMeasure>();

		HashMap<String,HashMap<String,ArrayList<String>>> yagoIntervalsUri = new FactGrouping().groupByEntity(goldstandard_facts);
		HashMap<String,ArrayList<String>> yagoIntervals=yagoIntervalsUri.get(Uri);
		if(yagoIntervals!=null){
		int relevantIntervals=yagoIntervals.size();
	
		double overlapPrec=0,overlapRecall=0,overlapFM=0;
		
		for (String obj: tempodefactoIntervals.keySet()){
			//System.out.println(Uri+" "+obj+" "+ tempodefactoIntervals.get(obj));
			HashSet<Interval> intervalsRedu = tempodefactoIntervals.get(obj);
			
			
			ArrayList<String> yagoInterval = yagoIntervals.get(obj);
			
			if(yagoInterval==null){
				relevantIntervals=relevantIntervals-1;
				}
			else{
			String yagoIntervalStr = Arrays.toString(yagoInterval.toArray());
			String yagoStart = yagoInterval.get(0);
			String yagoEnd = yagoInterval.get(1);
			if(yagoEnd.contains("NOW")){
				yagoEnd="2013";
			}
			MatrixCreator mpc= new MatrixCreator();
			
			
		String intervalsReduStr = null;
			for(Interval interv:intervalsRedu){
			double countTcorrect =0.0;
			long yearDistanceYago = 0,yearDistance,yearDistanceTot=0;
			intervalsReduStr = interv.toString();
			
				Date column = mpc.stringToLong(interv.getEnd());					
				Date row = mpc.stringToLong(interv.getStart());

				long millisecDistance = column.getTime()-row.getTime();
				long dayDistance= millisecDistance/(1000 * 60 * 60 * 24);
				yearDistance= dayDistance/365;
				yearDistance=yearDistance+1;
				yearDistanceTot=yearDistanceTot+yearDistance;

				
				if (yagoStart.contains("null")||yagoEnd.contains("null")){
					relevantIntervals=relevantIntervals-1;
				}
				else{
//System.out.println(Uri+" " +obj+" "+yagoStart+" "+yagoEnd);
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

						}
						else if (int1.duringInverse(int2)||int1.startsInverse(int2)||int1.finishesInverse(int2)){
							countTcorrect= countTcorrect+(double)yearDistanceYago;

						}
						else if (int1.precedes(int2)||int1.follows(int2)){
							if (countTcorrect!=0.0){}
							else{
								countTcorrect= 0.0;}

						}
						else if (int1.meets(int2)||int1.meetsInverse(int2)){
							countTcorrect= countTcorrect+1;

						}
						else{
							Calendar cal = Calendar.getInstance();
							if(int1.overlaps(int2)){
								Date i = row;
								while (i.before(yagoEndDt)||i.equals(yagoEndDt)){
									
									if((i.after(yagoStartDt)||i.equals(yagoStartDt))&&(i.before(column)||i.equals(column))){
										countTcorrect= countTcorrect+1;
									}
									else{

									}
									
									cal.setTime(i);
									cal.add(Calendar.YEAR, 1);
									i = cal.getTime();

								}
							}
							else if (int1.overlapsInverse(int2)){
								Date i = yagoStartDt;
								while (i.before(column)||i.equals(column)){
									
									if((i.after(row)||i.equals(row))&&(i.before(yagoEndDt)||i.equals(yagoEndDt))){
										countTcorrect= countTcorrect+1;
									}
									else{

									}
									cal.setTime(i);
									cal.add(Calendar.YEAR, 1);
									i = cal.getTime();
								}
							}
						}
						//System.out.println(Uri+" "+obj+" "+interv+" "+yearDistance+" "+yearDistanceYago+" "+overlap);
			}


		
			
		//values for a single temporal interval
		overlapPrec  = countTcorrect/yearDistanceTot;
		overlapRecall = countTcorrect/yearDistanceYago;
		if(overlapRecall==0&&overlapPrec==0){
			overlapFM  = 0;
		}
		else{
			overlapFM  = 2*(overlapPrec*overlapRecall)/(overlapPrec+overlapRecall);
		}
		
		QualityMeasure metrics=new QualityMeasure();
		metrics.add(QualityMeasure.Entry.SUBJECT, Uri);
		metrics.add(QualityMeasure.Entry.OBJECT, obj);
		metrics.add(QualityMeasure.Entry.INTERVAL, intervalsReduStr);
		metrics.add(QualityMeasure.Entry.GOLDSTANDARD, yagoIntervalStr);
		metrics.add(QualityMeasure.Entry.PRECISION,Double.toString(overlapPrec));
		metrics.add(QualityMeasure.Entry.RECALL,Double.toString(overlapRecall));
		metrics.add(QualityMeasure.Entry.fMEASURE,Double.toString(overlapFM));
		
		metricsObj.add(metrics);

		//System.out.println(Uri+","+obj+","+intervalsRedu+","+yagoInterval+","+metrics.get(Entry.PRECISION)+","+metrics.get(Entry.RECALL)+","+metrics.get(Entry.fMEASURE));
		pw.println(Uri+";"+obj+";"+intervalsRedu+";"+yagoInterval+";"+metrics.get(Entry.PRECISION)+";"+metrics.get(Entry.RECALL)+";"+metrics.get(Entry.fMEASURE));
			}
		}
		}
		}
		return metricsObj;
		
	}
	
	
	

}