package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.Evaluation_v2;
import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.Matcher;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.Normalization.NormalizationSelection;
import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.Reasoning.Interval;
import it.unimib.disco.Reasoning.Reasoning;
import it.unimib.disco.Selection.Selection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;
import org.apache.log4j.Logger;

public class TemporalIntervalCreatoScript_v2 {

private static Logger logger = Logger.getLogger(TemporalIntervalCreatoScript_v2.class);
	
public List<QualityMeasure> temporalFact(HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject,
		List<Fact> temporalDefactoFacts,HashMap<String,HashMap<String,List<Fact>>> goldstandard_facts,
		int normalizationType,int selection,int k, int x) throws FileNotFoundException{
		
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
		//golds standarard normalization 
		HashMap<String,HashMap<String,HashSet<Interval>>> goldstandard = new HashMap<String,HashMap<String,HashSet<Interval>>>();

		for (String u:goldstandard_facts.keySet()){
			HashMap<String,HashSet<Interval>> gsIntervals = new HashMap<String,HashSet<Interval>>();
			for (String o:goldstandard_facts.get(u).keySet()){
				List<Fact> f = goldstandard_facts.get(u).get(o);
				List<Interval> gs_interv= new ArrayList<Interval>();
				
				for(Fact fact: f){
					Interval interval = new Interval();
					interval.addStart(fact.get(Fact.Entry.YAGOSTART));
					interval.addEnd(fact.get(Fact.Entry.YAGOEND));
					gs_interv.add(interval);
				}
				 gsIntervals.put(o, new Reasoning().concatenateIntervals(gs_interv));
			}
			goldstandard.put(u, gsIntervals);
		}

	
		/******************RIM**************/
		HashMap<String, DateOccurrence [][]> maximalRIM =  new MatrixCreator().createMaximalRIM(new FactGrouping().createRIMvectors(groupedFactBySubjectObject));

		logger.info("Created maximal RIM");
		
		
		/******************lexical RIM**************/
		//HashMap<String, DateOccurrence [][]> lexicalRIM =  new MatrixCreator().createLexicalRIM(featuresUri);
		//logger.info("Created lexical RIM");
		
		/******************Normalization **************/
		List<Fact> factNormalized= new NormalizationSelection().normalize(normalizationType,temporalDefactoFacts);
		
		/******************Matching **************/
		HashMap<String,HashMap<String,List<Fact>>> groupFacts = new FactGrouping().groupBySubjectObject(factNormalized);
		Matcher ta = new Matcher();
		HashMap<String,HashMap<String,List<Interval>>> sub_obj_interval = new HashMap<String,HashMap<String,List<Interval>>>() ;
		
		try {
		for (String uri: maximalRIM.keySet()){
			HashMap<String,List<Interval>> obj_interval= new HashMap<String,List<Interval>>();
			
			HashMap<String,List<Fact>> objbasedgroupfacts = groupFacts.get(uri);
			if(objbasedgroupfacts!=null){
			

			for (String obj: objbasedgroupfacts.keySet()){
				List<Fact> f = objbasedgroupfacts.get(obj);
						
				DateOccurrence [][] matrixManhattanDuration = ta.matrixYearsDuration(maximalRIM.get(uri));
						
				pw.println(uri+" "+obj);
				//obj_interval.put(obj, ta.dcCalculator(normalizationType,f,matrixManhattanDuration,pw));	
				obj_interval.put(obj, ta.las(normalizationType,f,matrixManhattanDuration,pw));
			}
			sub_obj_interval.put(uri, obj_interval);
			}
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
	    HashMap<String,HashMap<String,List<Interval>>> tempodefactoIntervalsUri = new HashMap<String,HashMap<String,List<Interval>>>();
	    for (String uri:sub_obj_interval.keySet()){
	    	
	    	HashMap<String,List<Interval>> ls = new Selection().selection(selection, x, k,sub_obj_interval.get(uri));
	    	
			tempodefactoIntervalsUri.put(uri,ls);
				
		}
	    
	  //Concatenate intervals based on Allen's Algebra reasoning
	    logger.info("Reasoning function");
	    HashMap<String,HashMap<String,HashSet<Interval>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<Interval>>>();
	    
	    for (String uri:tempodefactoIntervalsUri.keySet()){
	    	HashMap<String,HashSet<Interval>> reasoningIntervals = new HashMap<String,HashSet<Interval>>();
	    	
	    	for(String obj:tempodefactoIntervalsUri.get(uri).keySet()){
	    	
	    		reasoningIntervals.put(obj, new Reasoning().concatenateIntervals(tempodefactoIntervalsUri.get(uri).get(obj)));
	    	}
	    	reasoningIntervalsUri.put(uri, reasoningIntervals);
	    }

	    Evaluation_v2 ev = new Evaluation_v2();
		List<QualityMeasure> evaluationResults = new ArrayList<QualityMeasure>();

		evaluationResults=ev.overlap(reasoningIntervalsUri,goldstandard,pw1);
				
	
	
		System.out.println(evaluationResults.size());
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


