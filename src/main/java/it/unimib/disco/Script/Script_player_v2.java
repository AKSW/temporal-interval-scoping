package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.Evaluation_v2;
import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.Matcher;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.Normalization.NormalizationSelection;
import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;
import it.unimib.disco.Reasoning.Reasoning;
import it.unimib.disco.Selection.Selection;
import it.unimib.disco.Utilities.Configuration;
import it.unimib.disco.Utilities.ConfigurationReader;
import it.unimib.disco.Utilities.WriteMatrixOutput;
import it.unimib.disco.Utilities.WriteOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;
import org.apache.log4j.Logger;

public class Script_player_v2 {
	private static Logger logger = Logger.getLogger(Script_player_v2.class);

	public static void main (String args []) throws FileNotFoundException{
		if (args.length < 4) {
			System.out.println("Use: java TemporalIntervalCreator <Resource list file> <temporal defacto output> <yago's gold standard>");
			System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList_in2.txt /sortbyplayer-labels-with-space_out_lionel.csv /goldStandard_30_entities.csv");
		} else {
			
			// Resource URI extraction
			List<Fact> dateRepository=new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
			HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject = new FactGrouping().groupBySubjectObject(dateRepository); //group temporal facts (s,p,t) by subject and object (t)
					

			// Read temporalDefacto facts
			List<Fact> temporalDefactoFacts = new ReadFiles().readTabSeparatedFileLS(new File(args[1]));
								
					
			//Read gold standard facts
			List<Fact> yagoFactsLS = new ReadFiles().readTabSeparatedFileLS(new File(args[2]));
			HashMap<String,HashMap<String,List<Fact>>> goldstandard_facts = new FactGrouping().groupBySubjectObject(yagoFactsLS);
			
			//Read parameters
			Configuration config = new ConfigurationReader().readConfiguration(new File(args[3]));
			
			//,normalization,selection,k,x 
		
		
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
						//System.out.println(gs_interv);
					}
					 gsIntervals.put(o, new Reasoning().concatenateIntervals(gs_interv));
				}
				goldstandard.put(u, gsIntervals);
			}

		
			/******************RIM**************/
			HashMap<String, DateOccurrence [][]> maximalRIM =  new MatrixCreator().createMaximalRIM(new FactGrouping().createRIMvectors(groupedFactBySubjectObject));
			logger.info("Created maximal RIM");
			
			/******************Normalization **************/
			List<Fact> factNormalized= new NormalizationSelection().normalize(config.get(Configuration.Entry.NORMALIZATION),temporalDefactoFacts);
			
			/******************Matching **************/
			HashMap<String,HashMap<String,List<Fact>>> groupFacts = new FactGrouping().groupBySubjectObject(factNormalized);
			Matcher ta = new Matcher();
			HashMap<String,HashMap<String,List<Interval>>> sub_obj_interval = new HashMap<String,HashMap<String,List<Interval>>>() ;
			

			for (String uri: maximalRIM.keySet()){
				
				HashMap<String,List<Interval>> obj_interval= new HashMap<String,List<Interval>>();
				
				HashMap<String,List<Fact>> objbasedgroupfacts = groupFacts.get(uri);
				if(objbasedgroupfacts!=null){
				

				for (String obj: objbasedgroupfacts.keySet()){
					List<Fact> f = objbasedgroupfacts.get(obj);
							
					DateOccurrence [][] matrixManhattanDuration = ta.matrixYearsDuration(maximalRIM.get(uri));
							
					obj_interval.put(obj, ta.las(config.get(Configuration.Entry.NORMALIZATION),f,matrixManhattanDuration));
				}
					sub_obj_interval.put(uri, obj_interval);
					
				}
			}
			new WriteMatrixOutput().printMatrix(sub_obj_interval);
		  
		    logger.info("Selection function");
		    HashMap<String,HashMap<String,List<Interval>>> tempodefactoIntervalsUri = new HashMap<String,HashMap<String,List<Interval>>>();
		    WriteOutput w = new WriteOutput();
		    w.writeIntervals(sub_obj_interval);
		    for (String uri:sub_obj_interval.keySet()){
		    	//selection, x, k
		    	HashMap<String,List<Interval>> ls = new Selection().selection(config.get(Configuration.Entry.SELECTION),config.get(Configuration.Entry.PROXY),config.get(Configuration.Entry.TOPK),sub_obj_interval.get(uri));
		    	
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

			evaluationResults=ev.overlap(reasoningIntervalsUri,goldstandard);


		try {
			BufferedWriter bw;
			File directory = new File (".");
			
			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_player.csv")));
			bw.write("subject"+"	"+"object"+"	"+"interval"+"	"+"goldstandard"+"	"+"precision"+"	"+"recall"+"	"+"microF"+"	"+"macroF"+"\n" );

			double avgP=0d, avgR=0d, avgF=0d;
			int total=0;
			
			for (QualityMeasure metrics:evaluationResults){
				
				double prec = Double.parseDouble(metrics.get(QualityMeasure.Entry.PRECISION));
				double rec = Double.parseDouble(metrics.get(QualityMeasure.Entry.RECALL));
				double fmes = Double.parseDouble(metrics.get(QualityMeasure.Entry.fMEASURE));
				
				bw.write(metrics.get(QualityMeasure.Entry.SUBJECT)+"	"+metrics.get(QualityMeasure.Entry.OBJECT)+"	"+metrics.get(QualityMeasure.Entry.INTERVAL)+"	"+metrics.get(QualityMeasure.Entry.GOLDSTANDARD)+"	"+prec+"	"+rec+"	"+fmes+"	"+" "+"\n" );
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
			double microF1=avgF/total;
			double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
			
			bw.write(" "+"	"+" "+"	"+" "+"	"+overlapPrec+"	"+overlapRec+"	"+microF1+"	"+macroF1+"\n" );
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	}
}
