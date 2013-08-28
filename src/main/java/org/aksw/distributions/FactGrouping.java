package org.aksw.distributions;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.aksw.distributions.Fact.Entry;

public class FactGrouping {
	
public HashMap<String,HashMap<String,List<Fact>>> groupBySubjectObject(List<Fact> listFromFact){
		
		HashMap<String,HashMap<String,List<Fact>>> res = new HashMap<String,HashMap<String,List<Fact>>>();
		HashMap<String,List<Fact>> groupByObj = new HashMap<String,List<Fact>>();
		List<Fact> facts = new ArrayList<Fact>();
		
		@SuppressWarnings("rawtypes")
		Iterator it = listFromFact.iterator();
		while (it.hasNext()){
			Fact f = new Fact();
			f = (Fact) it.next();
			
			if (!res.containsKey(f.get(Entry.SUBJECT))){

					groupByObj = new HashMap<String,List<Fact>>();
					res.put(f.get(Entry.SUBJECT), groupByObj);

			}
			
			groupByObj = res.get(f.get(Entry.SUBJECT));
			if(!groupByObj.containsKey(f.get(Entry.OBJECT))){
				
				facts = new ArrayList<Fact>();
				groupByObj.put(f.get(Entry.OBJECT),facts);
			}
			
			facts=groupByObj.get(f.get(Entry.OBJECT));
			facts.add(f); 
			
			groupByObj.put(f.get(Entry.OBJECT),facts);

			res.put(f.get(Entry.SUBJECT), groupByObj);
			
		 }
		
	
		/*for ( String str: res.keySet()){
			HashMap<String, HashSet<ArrayList<String>>> hm = res.get(str);
			for (String obj: hm.keySet()){
				System.out.println(str+" "+ obj+" "+ hm.get(obj));
			}
		}*/
		
		return res;
	}

	public HashMap<String,HashMap<String,ArrayList<String>>> groupBySubject(List<String> temporaldefacto){
		ReadFiles rf=new ReadFiles();
		HashSet<ArrayList<String>> file=rf.readTabSeparatedFile(temporaldefacto);
		
		HashMap<String,HashMap<String,ArrayList<String>>> resource = new HashMap<String,HashMap<String,ArrayList<String>>>();
		HashMap<String,ArrayList<String>> yagoIntervals= new HashMap<String,ArrayList<String>>(); 
		
		for (ArrayList<String> record:file){
			ArrayList<String> yagoInterval = new ArrayList<String>();
			if (!resource.containsKey(record.get(0))){
				yagoIntervals = new HashMap<String,ArrayList<String>>();
				resource.put(record.get(0), yagoIntervals);
			}
			if(yagoIntervals.containsKey(record.get(2))){
				yagoInterval = new ArrayList<String>();
				yagoIntervals.put(record.get(2), yagoInterval );
			} 
			
			yagoIntervals=resource.get(record.get(0));
			yagoInterval.add(record.get(3));
			yagoInterval.add(record.get(4));
			yagoIntervals.put(record.get(2),yagoInterval);
			
		resource.put(record.get(0), yagoIntervals);	
		}
		
		
		/*for ( String str: resource.keySet()){
			HashMap<String,ArrayList<String>> hm = resource.get(str);
			for (String y: hm.keySet()){
				System.out.println(str+" "+ y+" "+ hm.get(y));
			}
		}*/
		return resource;
	}


}
