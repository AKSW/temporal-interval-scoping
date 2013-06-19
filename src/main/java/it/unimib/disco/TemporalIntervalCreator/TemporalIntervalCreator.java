package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.Evaluation.Evaluation;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.FactExtractor.ResourceFetcher;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Reasoning;
import it.unimib.disco.Selection.Selection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;

public class TemporalIntervalCreator {

private static Logger logger = Logger.getLogger(TemporalIntervalCreator.class);
	
public static void main (String []args) throws FileNotFoundException{
	if (args.length < 1) {
		System.out.println("Use: java TemporalIntervalCreator <Resource list file> <temporal defacto output> <yago's gold standard>");
		System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList.txt /sortbyplayer-labels-with-space_out_medium.csv /gold_standard.csv");
	} else {
		
		// Resource URI extraction
		List<String> resURIs = ReadFiles.getURIs(TemporalIntervalCreator.class.getResource(args[0]).getFile()));
		logger.info("DBpedia resources list file parsed");

		
		// Read temporalDefacto facts
		List<String> temporalDefactoFacts = ReadFiles.getURIs(TemporalIntervalCreator.class.getResource(args[1]).getFile()));
		logger.info("TemporalDefacto facts parsed");
		
		
		//Read gold standard facts
		List<String> yagoFacts = ReadFiles.getURIs(TemporalIntervalCreator.class.getResource(args[2]).getFile()));
		logger.info("Yago facts parsed");
		

		ResourceFetcher rf = new ResourceFetcher();
		
		HashMap<String,OntModel> resourceModel =  rf.fetch(resURIs);
		logger.info("Retrieved all dbpedia's entity description");
		

		FileOutputStream fos = new FileOutputStream("temporalIntervalCreator_out2.csv");
		PrintWriter pw = new PrintWriter(fos);
		
		FileOutputStream fos1 = new FileOutputStream("evaluationWithIntervals.csv");
		PrintWriter pw1 = new PrintWriter(fos1);
		
		/***********Configuration setup****/
		int normalizationType= 1; // no-normalization =1, local-normalization=2, global-normalization =3, chi-square-normalization=4
		int selection= 1; // topK =1, proxy=2, combined =3
		int x=3;
		int k=1; //k>0;
		
		// date and fact extractor
		MatrixPruningCreator dfe = new MatrixPruningCreator();
					
		HashMap<String, HashMap<String, HashSet<String>>> repositoryDates =  dfe.fetchDates(resourceModel);
		logger.info("Retrieved all dbpedia's property value of type date");
		
		HashMap<String, DateOccurrence [][]> maximalMatrixDates =  dfe.maximalMatrixCreator(repositoryDates);
		
		
		HashMap<String, HashMap<ArrayList<String>,Integer>> timeAnnotated = dfe.temporalPredicateExtractor(repositoryDates,true);

			
		HashMap<String,DateOccurrence [][]> reducedMatrix1= dfe.matrixReducerSE(timeAnnotated,maximalMatrixDates);
	
		
		HashMap<String, HashMap<ArrayList<String>,Integer>> timeAnnotated1 = dfe.temporalPredicateExtractor(repositoryDates,false);
		logger.info("Retrieved all dbpedia's semantic predicates");
		
		HashMap<String,DateOccurrence [][]> reducedMatrix2 = dfe.matrixReducerSP(timeAnnotated1,reducedMatrix1);
		logger.info("Build matrix with starting and ending time points");
		
			
		NormalizationSelection ns = new NormalizationSelection();
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupOccu= ns.groupOccuByYear(normalizationType,temporalDefactoFacts);	
		
			
		TemporalIntervalFactAssociator ta = new TemporalIntervalFactAssociator();
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> sodc = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>() ;
		
		try {
		for (String uri: reducedMatrix2.keySet()){

					HashMap<String,HashSet<ArrayList<String>>> objYearOccu = groupOccu.get(uri);
					HashMap<String,HashSet<ArrayList<String>>> odc= new HashMap<String,HashSet<ArrayList<String>>>();
					
					for (String obj: objYearOccu.keySet()){
						HashSet<ArrayList<String>> yearOccs = objYearOccu.get(obj);
						
						DateOccurrence [][] matrixManhattanDuration = ta.matrixManhattanDuration(reducedMatrix2.get(uri));
						
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
			System.out.println(uri);
			tempodefactoIntervalsUri.put(uri,sel.selection(selection, x, k,objIntervals));
				
		}
	    
	    //Concatenate intervals based on Allen's Algebra reasoning
	    HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	    for (String uri:tempodefactoIntervalsUri.keySet()){
	    	HashMap<String,HashSet<ArrayList<String>>> reasoningIntervals = new HashMap<String,HashSet<ArrayList<String>>>();
	    	for(String obj:tempodefactoIntervalsUri.get(uri).keySet()){
	    		HashSet<ArrayList<String>> intervalSelected = tempodefactoIntervalsUri.get(uri).get(obj);
	    		Reasoning rs = new Reasoning();
	    		reasoningIntervals.put(obj, rs.concatenateIntervlas(intervalSelected));
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
      
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("evaluation.csv")));
			bw.write("subject"+","+"object"+","+"overlapPrec"+","+"overlapRecall"+","+"overlapF1"+"\n" );
			double avgP=0d, avgR=0d, avgF=0d;
			int total=0;
			for (String uri:evaluationResults.keySet()){
				
				HashMap<String,ArrayList<Double>> er=evaluationResults.get(uri);
				for (String obj: er.keySet()){
					ArrayList<Double> metrics = er.get(obj);
					bw.write(uri+","+obj+","+metrics.get(0)+","+metrics.get(1)+","+metrics.get(2)+"\n" );
					avgP=avgP+metrics.get(0);
					avgR=avgR+metrics.get(1);
					avgF=avgF+metrics.get(2);
					
					total++;
				}
				
			}
			bw.write(""+","+""+","+avgP/total+","+avgR/total+","+avgF/total+"\n" );
		
		
			bw.flush();
			bw.close();
//			logger.info("Results file built");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
}


