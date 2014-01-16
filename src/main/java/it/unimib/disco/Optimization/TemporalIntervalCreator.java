package it.unimib.disco.Optimization;

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


public class TemporalIntervalCreator {



public QualityMeasure measure(Configuration phenotype, HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject,
		List<Fact> temporalDefactoFacts,HashMap<String,HashMap<String,List<Fact>>> goldstandard_facts) throws FileNotFoundException  {
	
	//System.out.println(phenotype.getSelection()+" "+phenotype.getX()+" "+phenotype.getK()+" "+phenotype.getNormalization());
	 QualityMeasure m = new QualityMeasure();
	 FileOutputStream fos,fos1;
		File directory = new File (".");
		fos = new FileOutputStream(directory.getAbsolutePath()+"/output/matrix/"+"matrix_topK.csv");
		fos1 = new FileOutputStream(directory.getAbsolutePath()+"/output/interval/"+"evaluationWithIntervals_topK.csv");

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
		
		
		/******************Normalization **************/
		List<Fact> factNormalized= new NormalizationSelection().normalize(phenotype.getNormalization(),temporalDefactoFacts);
			
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
					
				phenotype.getNormalization();
				//obj_interval.put(obj, ta.dcCalculator(phenotype.getNormalization(),f,matrixManhattanDuration,pw));
				obj_interval.put(obj, ta.las(phenotype.getNormalization(),f,matrixManhattanDuration,pw));
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
		
	
		HashMap<String,HashMap<String,List<Interval>>> tempodefactoIntervalsUri = new HashMap<String,HashMap<String,List<Interval>>>();
		for (String uri:sub_obj_interval.keySet()){
			HashMap<String,List<Interval>> ls =new Selection().selection(phenotype.getSelection(), phenotype.getX(), phenotype.getK(),sub_obj_interval.get(uri));
				tempodefactoIntervalsUri.put(uri,ls);
				//System.out.println(uri+" "+ls+""+phenotype.getSelection());
		}
		        
		//Concatenate intervals based on Allen's Algebra reasoning
	
		 HashMap<String,HashMap<String,HashSet<Interval>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<Interval>>>();
		    
		    for (String uri:tempodefactoIntervalsUri.keySet()){
		    	HashMap<String,HashSet<Interval>> reasoningIntervals = new HashMap<String,HashSet<Interval>>();
		    	
		    	for(String obj:tempodefactoIntervalsUri.get(uri).keySet()){
		    		//System.out.println(uri+" "+obj+ " "+tempodefactoIntervalsUri.get(uri).get(obj));
		    		reasoningIntervals.put(obj, new Reasoning().concatenateIntervals(tempodefactoIntervalsUri.get(uri).get(obj)));
		    	}
		    	reasoningIntervalsUri.put(uri, reasoningIntervals);
		 }
		    
		Evaluation_v2 ev = new Evaluation_v2();
		List<QualityMeasure> evaluationResults = new ArrayList<QualityMeasure>();

		evaluationResults=ev.overlap(reasoningIntervalsUri,goldstandard,pw1);
		double avgP=0d, avgR=0d, avgF=0d;
		int total=0;

			for (QualityMeasure metrics:evaluationResults){
					
					double prec = Double.parseDouble(metrics.get(QualityMeasure.Entry.PRECISION));
					double rec = Double.parseDouble(metrics.get(QualityMeasure.Entry.RECALL));
					double fmes = Double.parseDouble(metrics.get(QualityMeasure.Entry.fMEASURE));
					
					
					if(Double.isNaN(prec)||Double.isNaN(rec)||Double.isNaN(fmes)){
					}
					else{
						avgP=avgP+prec;

						avgR=avgR+rec;
						avgF=avgF+fmes;
					
					total++;
					}
				}

				double overlapPrec=avgP/total;
				double overlapRec=avgR/total;
				//double microF1=avgF/total;
				double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
		m.add(QualityMeasure.Entry.PRECISION, String.valueOf(overlapPrec));
		m.add(QualityMeasure.Entry.RECALL, String.valueOf(overlapRec ));
		m.add(QualityMeasure.Entry.fMEASURE, String.valueOf(macroF1 ));
		
	     return m;
	}
}



