package it.unimib.disco.Script;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.MWBMatchingAlgorithm;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;
import it.unimib.disco.Reasoning.Reasoning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSimoneMWBM {

	public static void main(String[] args) throws FileNotFoundException
	{
		/***********Configuration setup****/
		//Interpretato come in top-k
		int k=3; //k>0;
		/***********Configuration end****/

		//lettura file
		File file = new File("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/matrix/matrix_proxyX-1-10.csv");
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
		HashMap<String, HashMap<String, List<Interval>>> subject_object_intervals = new HashMap<String, HashMap<String, List<Interval>>>();
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
					subject_object_intervals.put(subject, obj_intervals);
				}
				else if (j<objects.length)
				{
					List<Interval> intervals_list = new ArrayList<Interval>();
					intervals_list.add(intervals[results[j]]);
					subject_object_intervals.get(subject).put(objects[j], intervals_list);
				}
				else
				{
					int index = j%objects.length;
					subject_object_intervals.get(subject).get(objects[index]).add(intervals[results[j]]);
				}
			}
		}
		
		//Reasoning
	    HashMap<String,HashMap<String,HashSet<Interval>>> reasoningIntervalsUri = new HashMap<String,HashMap<String,HashSet<Interval>>>();
	    
	    for (String uri:subject_object_intervals.keySet()){
	    	HashMap<String,HashSet<Interval>> reasoningIntervals = new HashMap<String,HashSet<Interval>>();
	    	
	    	for(String obj:subject_object_intervals.get(uri).keySet()){
	    	
	    		reasoningIntervals.put(obj, new Reasoning().concatenateIntervals(subject_object_intervals.get(uri).get(obj)));
	    	}
	    	reasoningIntervalsUri.put(uri, reasoningIntervals);
	    }
	    
		//Salvataggio dati
	    FileOutputStream fos = new FileOutputStream("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/" + k + "_Outputs.csv");
		PrintWriter pw = new PrintWriter(fos);
		
		for (String subject: reasoningIntervalsUri.keySet())
		{
			HashMap<String, HashSet<Interval>> reasoningIntervals = reasoningIntervalsUri.get(subject);
			for (String object: reasoningIntervals.keySet())
			{
				Set<Interval> my_intervals = reasoningIntervals.get(object);
				pw.println(subject + " " + object);
				for (Interval interval: my_intervals)
				{
					pw.println(interval.toString());
				}
			}
		}
		
		pw.close();
		try {
			fos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    
	}
}
