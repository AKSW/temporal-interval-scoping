package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.Evaluation;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Reasoning.Reasoning;
import it.unimib.disco.Selection.Selection;
import it.unimib.disco.TemporalIntervalCreator.MatrixCreator_Copy;
import it.unimib.disco.TemporalIntervalCreator.NormalizationSelection;
import it.unimib.disco.TemporalIntervalCreator.TemporalIntervalFactAssociator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;


public class TemporalIntervalCreatoScriptTD {

private static Logger logger = Logger.getLogger(TemporalIntervalCreatoScriptTD.class);
	
public HashMap<String,HashMap<String,ArrayList<Double>>> temporalFact(List<String> resURIs,List<String> temporalDefactoFacts,List<String> yagoFacts,int normalizationType,int selection,int k, int x) throws FileNotFoundException{

		
		FileOutputStream fos,fos1;
		File directory = new File (".");
		if(selection==1){
			fos = new FileOutputStream(directory.getAbsolutePath()+"/output/matrix/"+"matrix_topK"+"-"+k+"-"+x+".csv");
			fos1 = new FileOutputStream(directory.getAbsolutePath()+"/output/interval/"+"evaluationWithIntervals_topK"+"-"+k+"-"+x+".csv");
			
		}
		else if(selection==2){
			fos = new FileOutputStream(directory.getAbsolutePath()+"/output/matrix/"+"matrix_proxyX"+"-"+k+"-"+x+".csv");
			fos1 = new FileOutputStream(directory.getAbsolutePath()+"/output/interval/"+"evaluationWithIntervals_proxyX"+"-"+k+"-"+x+".csv");
			
		}
		else{
			fos = new FileOutputStream(directory.getAbsolutePath()+"/output/matrix/"+"matrix_neighbor"+"-"+k+"-"+x+".csv");
			fos1 = new FileOutputStream(directory.getAbsolutePath()+"/output/interval/"+"evaluationWithIntervals_neighbor"+"-"+k+"-"+x+".csv");
			
		}
		

		PrintWriter pw = new PrintWriter(fos);
		PrintWriter pw1 = new PrintWriter(fos1);
		
	
		// date and fact extractor
		MatrixCreator_Copy dfe = new MatrixCreator_Copy();
					
		HashMap<String, HashMap<String, HashSet<String>>> repositoryDates =  dfe.fetchDatesTD(temporalDefactoFacts);
		logger.info("Retrieved all time points");
		
		/******************RIM**************/
		HashMap<String, DateOccurrence [][]> maximalRIM =  dfe.createMaximalRIM(repositoryDates);
		logger.info("Created maximal RIM");
		
		/******************adjust RIM based on the "start/end timepoints" evidence **************/
		HashMap<String, HashMap<ArrayList<String>,Integer>> timepointsAnnotatedSE = dfe.temporalPredicateExtractor(repositoryDates,true);			
		HashMap<String,DateOccurrence [][]> adjustedRIM_startend= dfe.adjustRIM_SE(timepointsAnnotatedSE,maximalRIM);
		logger.info("Adjusted RIM matrix with start-end predicates");
		/*for (String u: reducedMatrix1.keySet()){
			DateOccurrence [][] a=reducedMatrix1.get(u);
			System.out.println(u);
			for (int i=0; i<a.length;i++){
				for (int j=0;j<a[i].length;j++){
				
					System.out.print(a[i][j].getDate()+""+ a[i][j].getOccurrence()+";");
				}
			
				System.out.println();

			}
		}		*/
		

		/******************adjust RIM based on the "predicate semantic of each timepoint" evidence **************/
		HashMap<String, HashMap<ArrayList<String>,Integer>> timepointsAnnotatedRP = dfe.temporalPredicateExtractor(repositoryDates,false);		
		HashMap<String,DateOccurrence [][]> adjustedRIM_root = dfe.adjustRIM_R(timepointsAnnotatedRP,adjustedRIM_startend);
		logger.info("Adjusted RIM matrix with root predicates");
		
		NormalizationSelection ns = new NormalizationSelection();
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupOccu= ns.groupOccuByYear(normalizationType,temporalDefactoFacts);	
		
			
		TemporalIntervalFactAssociator ta = new TemporalIntervalFactAssociator();
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> sodc = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>() ;
		
		try {
		for (String uri: adjustedRIM_root.keySet()){

					HashMap<String,HashSet<ArrayList<String>>> objYearOccu = groupOccu.get(uri);
					HashMap<String,HashSet<ArrayList<String>>> odc= new HashMap<String,HashSet<ArrayList<String>>>();
					
					for (String obj: objYearOccu.keySet()){
						HashSet<ArrayList<String>> yearOccs = objYearOccu.get(obj);
						
						DateOccurrence [][] matrixManhattanDuration = ta.matrixManhattanDuration(adjustedRIM_root.get(uri));
						
						pw.println(uri+" "+obj);
						odc.put(obj, ta.dcCalculator(normalizationType,yearOccs,matrixManhattanDuration,pw));
						
					}
					sodc.put(uri, odc);
			}
	} catch (NullPointerException e) {
		e.printStackTrace();
	}
	pw.close();
	try {
		fos.close();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	    
	    logger.info("Selection function");
	  
	    HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> tempodefactoIntervalsUri = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	    for (String uri:sodc.keySet()){
			HashMap<String,HashSet<ArrayList<String>>> objIntervals=sodc.get(uri);

			Selection sel= new Selection();
			
			tempodefactoIntervalsUri.put(uri,sel.selection(selection, x, k,objIntervals));
				
		}
	    
	    //Concatenate intervals based on Allen's Algebra reasoning
	    HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	    for (String uri:tempodefactoIntervalsUri.keySet()){
	    	HashMap<String,HashSet<ArrayList<String>>> reasoningIntervals = new HashMap<String,HashSet<ArrayList<String>>>();
	    	for(String obj:tempodefactoIntervalsUri.get(uri).keySet()){
	    		HashSet<ArrayList<String>> intervalSelected = tempodefactoIntervalsUri.get(uri).get(obj);
	    		Reasoning rs = new Reasoning();
	    		reasoningIntervals.put(obj, rs.concatenateIntervals(intervalSelected));
	    	}
	    	reasoningIntervalsUri.put(uri, reasoningIntervals);
	    }

	    
		Evaluation ev = new Evaluation();
		HashMap<String,HashMap<String,ArrayList<Double>>> evaluationResults = new HashMap<String,HashMap<String,ArrayList<Double>>>();
		try {
			
			for (String uri:reasoningIntervalsUri.keySet()){
				HashMap<String,HashSet<ArrayList<String>>> tempodefactoIntervals=reasoningIntervalsUri.get(uri);
				
				HashMap<String,ArrayList<Double>> localmetrics=ev.overlap(uri,tempodefactoIntervals,yagoFacts,pw1);
				evaluationResults.put(uri,localmetrics);
	
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		pw1.close();
		try {
			fos1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
		return evaluationResults;
	}

}


