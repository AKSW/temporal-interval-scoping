package it.unimib.disco.Script;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.MWBMatchingAlgorithm;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSimoneMWBM_No_Overlap {

	public static void main(String[] args) throws FileNotFoundException
	{
		/***********Configuration setup****/
		//Per gli oggetti che contengono queste parole non ha senso procedere con l'algoritmo globale con intervalli disgiunti
		String filterWords = "national_football_team"; 
		
		/***********Configuration end****/
		//Mappa soggetti-oggetti-intervalli
		HashMap<String, HashMap<String, List<Interval>>> subject_object_intervals = new HashMap<String, HashMap<String, List<Interval>>>();

		//lettura file
		File file = new File("C:\\Users\\HBK\\Documents\\University\\AI\\matrix_proxyX-1-10.csv");
		HashMap<String, HashMap<String, DateOccurrence[][]>> subject_object_matrix = new ReadFiles().readTabSeparatedMatrixFile(file);
		
		//Mappe intermedie di valori globali soggetto -> valore
		HashMap<String, Interval[]> subject_intervals = new HashMap<String,Interval[]>();
		HashMap<String, String[]> subject_objects = new HashMap<String, String[]>();
		HashMap<String, MWBMatchingAlgorithm> subject_values = new HashMap<String, MWBMatchingAlgorithm>();
		
		//Riempimento mappa soggetti-oggetti-matrici e impostazione parametri di matching
		for (String subject: subject_object_matrix.keySet())
		{
			HashMap<String, DateOccurrence[][]> obj_matrix = subject_object_matrix.get(subject);
			HashMap<String, List<Interval>> obj_intervals = new HashMap<String, List<Interval>>();
			subject_object_intervals.put(subject, obj_intervals);
			
			if (obj_matrix.size() > 0)
			{
				List<String> myobj = new ArrayList<String>();
				for (String object: obj_matrix.keySet())
				{
					if (!object.contains(filterWords))
					{
						myobj.add(object);
					}
				}
				String[] objects = new String[myobj.size()];
				objects = (String[]) myobj.toArray(objects);
				if (objects.length > 0)
				{
					Interval[] intervals = new Interval[obj_matrix.get(objects[0]).length];
					int n = objects.length;
					MWBMatchingAlgorithm MWBmatcher = new MWBMatchingAlgorithm(intervals.length-1, objects.length*intervals.length-1);
										
					int count_obj = 0;
					int count_int = 0;
					for (String object: objects)
					{
						count_int = 0;
						DateOccurrence[][] matrix = obj_matrix.get(object);
						for (int i=1; i < matrix.length; i++)
						{
							Interval interval = new Interval();
							interval.addStart(matrix[i][0].getDate());
							interval.addEnd(matrix[0][i].getDate());
							intervals[count_int] = interval;
							for (int count_k = 0; count_k<intervals.length-1; count_k++)
							{
								double w = Double.parseDouble(matrix[i][i].getOccurrence());
								if (w <= 1e-10)
								{
									MWBmatcher.setWeight(count_int, count_obj + (n*count_k), 1.0);
								} else
								{
									MWBmatcher.setWeight(count_int, count_obj + (n*count_k), w + 1.0);
								}
							}
							count_int++;
						}
						count_obj++;
					}
					subject_intervals.put(subject, intervals);
					subject_objects.put(subject, objects);
					subject_values.put(subject, MWBmatcher);
				}
			}
		}
		
		//Calcolo delle soluzioni e riempimento mappa intervalli
		for (String subject: subject_values.keySet())
		{
			MWBMatchingAlgorithm MWBmatcher = subject_values.get(subject);
			int[] matching_results = MWBmatcher.getMatching();
			String[] objects = subject_objects.get(subject);
			Interval[] intervals = subject_intervals.get(subject);
			
			for (int i = 0; i<objects.length; i++){
				List<Interval> obj_intervals_list = new ArrayList<Interval>();
				subject_object_intervals.get(subject).put(objects[i], obj_intervals_list);
			}

			for (int i = 0; i<matching_results.length;i++){
				if (matching_results[i] >= 0)
				{
					String object = objects[matching_results[i]%objects.length];
					if (MWBmatcher.getWeight(i, matching_results[i]) > 1.0)
					{
						subject_object_intervals.get(subject).get(object).add(intervals[i]);
					}
				}
			}
		}

		//Salvataggio dati
	    FileOutputStream fos = new FileOutputStream("C:\\Users\\HBK\\Documents\\University\\AI\\MWBM_No_Overlap_Date_" + "_Outputs.csv");
		PrintWriter pw = new PrintWriter(fos);
		
		for (String subject: subject_object_intervals.keySet())
		{
			HashMap<String, List<Interval>> objIntervals = subject_object_intervals.get(subject);
			for (String object: objIntervals.keySet())
			{
				List<Interval> my_intervals= objIntervals.get(object);
				if (my_intervals != null && !my_intervals.isEmpty())
				{
					pw.println(subject + " " + object);
					for (Interval interval: my_intervals)
					{
						pw.println(interval.toString());
					}
				}
			}
		}
		pw.close();
		try {
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}