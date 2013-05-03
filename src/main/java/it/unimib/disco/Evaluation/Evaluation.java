package it.unimib.disco.Evaluation;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.TemporalIntervalCreator.MatrixPruningCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Evaluation {

	public HashMap<String,ArrayList<Double>> precision(HashMap<String,DateOccurrence [][]> matrix, List<String> yagoFacts){
		HashMap<String,ArrayList<Double>> localPrecisionURI = new HashMap<String,ArrayList<Double>>();

		
		
		HashMap<String,HashSet<ArrayList<String>>> yagoIntervalsUri = groupByEntity(yagoFacts);
		double localPrecision=0.0,localRecall=0.0;
		
		for (String Uri: matrix.keySet()){
			DateOccurrence [][] temporalDCMatrix= matrix.get(Uri);
			HashSet<ArrayList<String>> yagoIntervals=yagoIntervalsUri.get(Uri);
			int relevantIntervals=0,relevAndRetriev = 0,retrievedIntervals=0;
			ArrayList<Double> metrics=new ArrayList<Double>();

			for (int i=1; i<temporalDCMatrix.length; i++){
				for(int j=1; j<temporalDCMatrix[i].length; j++){
							
					if(j>=i){
						if(!(temporalDCMatrix[i][j].getOccurrence().get(0).equalsIgnoreCase("0"))){
							
							retrievedIntervals++;

							for (ArrayList<String> yagoInterval: yagoIntervals){
								relevantIntervals=yagoIntervals.size();
								
								MatrixPruningCreator mpc= new MatrixPruningCreator();
								Date column = mpc.stringToLong(temporalDCMatrix[0][j].getDate());
								Date row = mpc.stringToLong(temporalDCMatrix[i][0].getDate());

								if(row.equals(mpc.stringToLong(yagoInterval.get(0)))&&column.equals(mpc.stringToLong(yagoInterval.get(1)))){
									relevAndRetriev++;
									
								}

							}
						}
					
					}
					
				}
			}
			localPrecision = relevAndRetriev*1.0/retrievedIntervals;
			localRecall = relevAndRetriev*1.0/relevantIntervals;
			metrics.add(localPrecision);
			metrics.add(localRecall);
			localPrecisionURI.put(Uri, metrics);
		}
		
		return localPrecisionURI;
		
	}
	
	public HashMap<String,HashSet<ArrayList<String>>> groupByEntity(List<String> temporaldefacto){
		ReadFiles rf=new ReadFiles();
		HashSet<ArrayList<String>> file=rf.readCommaSeparatedFile(temporaldefacto);
		
		HashMap<String,HashSet<ArrayList<String>>> resource = new HashMap<String,HashSet<ArrayList<String>>>();
		HashSet<ArrayList<String>> yagoIntervals= new HashSet<ArrayList<String>>(); 
		
		for (ArrayList<String> record:file){
			
			if (!resource.containsKey(record.get(0))){
				yagoIntervals = new HashSet<ArrayList<String>>();
				resource.put(record.get(0), yagoIntervals);
			}
			ArrayList<String> yagoInterval = new ArrayList<String>();
			yagoInterval.add(record.get(3));
			yagoInterval.add(record.get(4));
			yagoIntervals.add(yagoInterval);
			
		resource.put(record.get(0), yagoIntervals);	
		}
		
		
		/*for ( String str: resource.keySet()){
			HashMap<String, Integer> hm = resource.get(str);
			for (String y: hm.keySet()){
				System.out.println(str+" "+ y+" "+ hm.get(y));
			}
		}*/
		return resource;
	}
}
