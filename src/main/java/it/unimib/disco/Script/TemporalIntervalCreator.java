package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.Evaluation;
import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.Matcher;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.Normalization.NormalizationSelection;
import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;
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

public class TemporalIntervalCreator {

private static Logger logger = Logger.getLogger(TemporalIntervalCreator.class);
	
public static void main (String []args) throws FileNotFoundException{
	if (args.length < 1) {
		System.out.println("Use: java TemporalIntervalCreator <temporal facts> <temporal defacto output> <gold standard>");
		System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList.txt /sortbyplayer-labels-with-space_out_medium.csv /gold_standard.csv");
	} else {
		
		
		// Resource URI extraction
		List<Fact> dateRepository=new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
		//dateRepository= new ReadFiles().csv(new File(args[0]));
		HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject = new FactGrouping().groupBySubjectObject(dateRepository); //group temporal facts (s,p,t) by subject and object (t)
		logger.info("Temporal fact list file parsed");

		// Read temporalDefacto facts
		List<Fact> l = new ReadFiles().readTabSeparatedFileLS(new File(args[1]));
		/*List<String> temporalDefactoFacts = ReadFiles.getURIs(new File(args[1]));
		List<Fact> l = new ArrayList<Fact>();
		l = new ReadFiles().creatListOfFacts(temporalDefactoFacts);*/
		
		logger.info("TemporalDefacto facts parsed");
		
		
		//Read gold standard facts
		List<String> goldstandard_facts = ReadFiles.getURIs(new File(args[2]));
		logger.info("Yago facts parsed");
		
		FileOutputStream fos = new FileOutputStream("temporalIntervalCreator_out2.csv");
		PrintWriter pw = new PrintWriter(fos);
		
		FileOutputStream fos1 = new FileOutputStream("evaluationWithIntervals.csv");
		PrintWriter pw1 = new PrintWriter(fos1);
		//3, 1, 1, 2
		/***********Configuration setup****/
		int normalizationType=2; // no-normalization =1, local-normalization=2, global-normalization =3, chi-square-normalization=4
		int selection=1; // topK =1, proxy=2, combined =3
		int x=0;
		int k=4; //k>0;
		

		//******************RIM**************//*
		HashMap<String, DateOccurrence [][]> maximalRIM =  new MatrixCreator().createMaximalRIM(new FactGrouping().createRIMvectors(groupedFactBySubjectObject));
		/*for (String uri: maximalRIM.keySet()){
		for (int i=0; i<maximalRIM.get(uri).length;i++){
			for (int j=0;j<maximalRIM.get(uri)[i].length;j++){
				
				System.out.print(maximalRIM.get(uri)[i][j].getDate()+""+ maximalRIM.get(uri)[i][j].getOccurrence()+";");
			}
			System.out.println();

	}}*/
		logger.info("Created maximal RIM");
					
		//******************Normalization **************/
		List<Fact> factNormalized= new NormalizationSelection().normalize(normalizationType,l);
		
		//******************Matching **************//*
		HashMap<String,HashMap<String,List<Fact>>> groupFacts = new FactGrouping().groupBySubjectObject(factNormalized);
		
		Matcher ta = new Matcher();
		HashMap<String,HashMap<String,List<Interval>>> sub_obj_interval = new HashMap<String,HashMap<String,List<Interval>>>() ;
		
		try {
		for (String uri: maximalRIM.keySet()){
			HashMap<String,List<Interval>> obj_interval= new HashMap<String,List<Interval>>();
			HashMap<String,List<Fact>> objbasedgroupfacts = groupFacts.get(uri);
			if (objbasedgroupfacts!=null){
			for (String obj: objbasedgroupfacts.keySet()){
				List<Fact> f = objbasedgroupfacts.get(obj);

				DateOccurrence [][] matrixYearsDuration = ta.matrixYearsDuration(maximalRIM.get(uri));
						
				pw.println(uri+" "+obj);
				obj_interval.put(obj, ta.dcCalculator(normalizationType,f,matrixYearsDuration,pw));
			
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
			
			tempodefactoIntervalsUri.put(uri,new Selection().selection(selection, x, k,sub_obj_interval.get(uri)));
				
		}
   
	  //Concatenate intervals based on Allen's Algebra reasoning
	    logger.info("Reasoning function");
	    HashMap<String,HashMap<String,HashSet<Interval>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<Interval>>>();
	    
	    Evaluation ev = new Evaluation();
		List<QualityMeasure> evaluationResults = new ArrayList<QualityMeasure>();
		try {
			
			for (String uri:reasoningIntervalsUri.keySet()){
				HashMap<String,HashSet<Interval>> timeintervals=reasoningIntervalsUri.get(uri);
				
				List<QualityMeasure> localmetrics=ev.overlap(uri,timeintervals,goldstandard_facts,pw1);
				for (int i = 0; i < localmetrics.size(); i++) {
					evaluationResults.add(localmetrics.get(i).copy());
				}
	
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println(evaluationResults.size());
		pw1.close();
		try {
			fos1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}
	}

}


