package it.unimib.disco.Normalization;

import it.unimib.disco.ReadFiles.FactGrouping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.distributions.Fact;
import org.aksw.distributions.LocalNormalization;
import org.aksw.distributions.Normalization;
import org.aksw.distributions.Fact.Entry;

public class TfIdfNormalization implements Normalization {
	
    public List<Fact> normalize(List<Fact> facts) {

    	List<Fact> copy = new ArrayList<Fact>();
        for (int i = 0; i < facts.size(); i++) {
            copy.add(facts.get(i).copy());
        }
        
        copy = localNormalizationFrequency(copy);
        
        //first get all dates
        Set<String> dates = new HashSet<String>();
        Set<String> objects = new HashSet<String>();
        for (Fact f : copy) {
            dates.add(f.get(Entry.DATE));
            objects.add(f.get(Entry.OBJECT));
        }       

        //build a map for each date
        for (String date : dates) {
            HashMap<String, Integer> date_scores = new HashMap<String, Integer>();
            for (Fact f : copy) {
                if (f.get(Entry.DATE).equals(date)) {
                    String score = f.get(Entry.SCORE);
                    if (Double.parseDouble(score) > 1e-10)
                    {
                    	if (date_scores.containsKey(date))
                		{
                			date_scores.put(date, date_scores.get(date)+1);
                		}
                    	else  date_scores.put(date,1);
                    }
                }
            }

            for (Fact f : copy) {
                if (f.get(Entry.DATE).equals(date)) {
                    String score = f.get(Entry.SCORE);
                    if (Double.parseDouble(score) > 1e-10 && date_scores.get(date) > 1e-10)
                    {
                        /*System.out.println(f.get(Entry.SUBJECT) + " " + f.get(Entry.OBJECT) + " " + f.get(Entry.SCORE) + " "  + f.get(Entry.YAGOSTART) + " " + f.get(Entry.YAGOEND) + " " + f.get(Entry.DATE) + " "+  objects.size() + " " + date_scores.get(date) + " " + (
                        		(Math.log(
                				(double) objects.size() / (double) date_scores.get(date)
                				+ 1.0) 
                		/ Math.log(objects.size())
                		) ));*/
                    	//td-idf = tf * log2()
                        //la scelta della base è arbitraria. Qui uso base objects.size() ovvero numero di squadre.
                        //qui implementato con la formula del cambiamento di base.
                        f.add(
                        	Entry.SCORE, //Double.parseDouble(score) * 
                        		(Math.log(
                        				(double) objects.size() / (double) date_scores.get(date)
                        				+ 1.0
                        				) 
                        		/ Math.log(objects.size())
                        		) 
                        	+ "");
                    } else
                    {
                    	f.add(Entry.SCORE, 0.0 + "");
                    }
                }
            }
        }
        
        return copy;
    }
    
	//local normalization function
	public List<Fact> localNormalizationFrequency(List<Fact> listOfFacts){
		List<Fact> factsGNs = new ArrayList<Fact>();
		List<Fact> copy = new ArrayList<Fact>();  
		
		HashMap<String,HashMap<String,List<Fact>>> res = new HashMap<String,HashMap<String,List<Fact>>>();
		FactGrouping group = new FactGrouping();
		res=group.groupBySubjectObject(listOfFacts);

		for (String u: res.keySet()){
			List<Fact> facts = new ArrayList<Fact>();
			
			HashMap<String,List<Fact>> objgrouping= res.get(u);
			
			for (String o: objgrouping.keySet()){
			
				for (Fact f: objgrouping.get(o)){
					
			        facts.add(f);

		        }
			}

			factsGNs = (new LocalNormalization()).normalize(facts);
			
	        for (int i = 0; i < factsGNs.size(); i++) {
	            copy.add(factsGNs.get(i));
	        }

		}

		/*for (Fact f:copy){
			System.out.println(f);
		}*/
		return copy;
	}
}
