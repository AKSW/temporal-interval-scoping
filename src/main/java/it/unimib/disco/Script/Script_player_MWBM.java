package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.Evaluation_v2;
import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.MWBMatchingAlgorithm;
import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;
import it.unimib.disco.Reasoning.Reasoning;

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

public class Script_player_MWBM {
	private static Logger logger = Logger.getLogger(Script_player_MWBM.class);

	public static void main (String args []) throws FileNotFoundException{
		if (args.length < 1) {
			System.out.println("Use: java TemporalIntervalCreator <Resource list file> <temporal defacto output> <yago's gold standard>");
			System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList_in2.txt /sortbyplayer-labels-with-space_out_lionel.csv /goldStandard_30_entities.csv");
		} else {
											
			//Read gold standard facts
			List<Fact> yagoFactsLS = new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
			HashMap<String,HashMap<String,List<Fact>>> goldstandard_facts = new FactGrouping().groupBySubjectObject(yagoFactsLS);

		
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

		
			/***********Configuration setup****/
			//Interpretato come in top-k
			int k=3; //k>0;
			/***********Configuration end****/
	
			//lettura file
			File file = new File("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/matrix/matrix.csv");
			HashMap<String, HashMap<String, DateOccurrence[][]>> subject_object_matrix = new ReadFiles().readTabSeparatedMatrixFile(file);

			
			//Mappe intermedie di valori globali soggetto -> valore
			HashMap<String, Interval[]> subject_intervals = new HashMap<String,Interval[]>();
			HashMap<String, String[]> subject_objects = new HashMap<String, String[]>();
			HashMap<String, MWBMatchingAlgorithm> subject_values = new HashMap<String, MWBMatchingAlgorithm>();
			
			//Riempimento mappa soggetti-oggetti-matrici e impostazione parametri di matching
			for (String subject: subject_object_matrix.keySet())
			{
				HashMap<String, DateOccurrence[][]> obj_matrix = subject_object_matrix.get(subject);
				if (obj_matrix.size() > 0)
				{
					String[] objects = new String[obj_matrix.size()];
					int obj_i = 0;
					for (String object: obj_matrix.keySet())
					{
						objects[obj_i] = object;
						obj_i++;
					}
					Interval[] intervals = new Interval[obj_matrix.get(objects[0]).length * (obj_matrix.get(objects[0]).length-1) / 2];
					int n = obj_matrix.size();
					MWBMatchingAlgorithm MWBmatcher = new MWBMatchingAlgorithm(obj_matrix.size()*k, intervals.length);
													
					int count_obj = 0;
					int count_int = 0;
					for (String object: obj_matrix.keySet())
					{
						count_int = 0;
						objects[count_obj] = object;
						DateOccurrence[][] matrix = obj_matrix.get(object);
						for (int i=1; i < matrix.length; i++)
						{
							for (int j=i; j < matrix[0].length; j++)
							{
								if (count_obj == 0)
								{
									Interval interval = new Interval();
									interval.addStart(matrix[i][0].getDate());
									interval.addEnd(matrix[0][j].getDate());
									intervals[count_int] = interval;
								}
								for (int count_k = 0; count_k<k; count_k++)
								{
									double w = Double.parseDouble(matrix[i][j].getOccurrence());
									if (w <= 1e-10)
									{
										MWBmatcher.setWeight(count_obj + (n*count_k), count_int,1.0);
									} else
									{
										MWBmatcher.setWeight(count_obj + (n*count_k), count_int, w+1.0);
									}
								}
								count_int++;
							}
						}
						count_obj++;
					}
					subject_intervals.put(subject, intervals);
					subject_objects.put(subject, objects);
					subject_values.put(subject, MWBmatcher);
				}
			}
			
			//Calcolo delle soluzioni e riempimento mappa intervalli
			HashMap<String, int[]> string_matching_results = new HashMap<String, int[]>();
			for (String subject: subject_values.keySet())
			{
				MWBMatchingAlgorithm MWBmatcher = subject_values.get(subject);
				int[] matching_results = MWBmatcher.getMatching();
				string_matching_results.put(subject, matching_results);
			}
			HashMap<String, HashMap<String, List<Interval>>> tempodefactoIntervalsUri = new HashMap<String, HashMap<String, List<Interval>>>();
			for (String subject: string_matching_results.keySet())
			{
				int[] results = string_matching_results.get(subject);
				String[] objects = subject_objects.get(subject);
				Interval[] intervals = subject_intervals.get(subject);
				for (int j=0;j<results.length;j++)
				{
					if (j==0)
					{
						List<Interval> intervals_list = new ArrayList<Interval>();
						intervals_list.add(intervals[results[j]]);
						HashMap<String, List<Interval>> obj_intervals = new HashMap<String, List<Interval>>();
						obj_intervals.put(objects[j], intervals_list);
						tempodefactoIntervalsUri.put(subject, obj_intervals);
					}
					else if (j<objects.length)
					{
						List<Interval> intervals_list = new ArrayList<Interval>();
						intervals_list.add(intervals[results[j]]);
						tempodefactoIntervalsUri.get(subject).put(objects[j], intervals_list);
					}
					else
					{
						int index = j%objects.length;
						tempodefactoIntervalsUri.get(subject).get(objects[index]).add(intervals[results[j]]);
					}
				}
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
			
			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_player_MWBM.csv")));
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
