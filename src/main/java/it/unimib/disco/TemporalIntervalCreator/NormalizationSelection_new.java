package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.ChiSquaredGlobalNormalization;
import org.aksw.distributions.Fact;
import org.aksw.distributions.FactReader;
import org.aksw.distributions.GlobalNormalization;

public class NormalizationSelection {

	ReadFiles rf=new ReadFiles();
	public HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupOccuByYear(int normalizationType, List<String> temporaldefacto){
		
		
		HashSet<ArrayList<String>> file=rf.readCommaSeparatedFile(temporaldefacto);
		HashSet<List<String>> fileNormalized= new HashSet<List<String>>();

		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	
		HashMap<String,HashSet<ArrayList<String>>> yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
		
		HashSet<ArrayList<String>> yearOccs = new HashSet<ArrayList<String>>();
		
		//local or no-normalization
		if(normalizationType==1||normalizationType==2){
			for (List<String> record:file){
				
				ArrayList<String> yO = new ArrayList<String>();
				//System.out.println(record.get(0)+" "+record.get(2));
				if (!res.containsKey(record.get(0))){
					yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
					res.put(record.get(0), yearOccu);
				}
				
				yearOccu=res.get(record.get(0));
				if(!yearOccu.containsKey(record.get(2))){
					
					yearOccs = new HashSet<ArrayList<String>>();
					yearOccu.put(record.get(2),yearOccs);
				}
				
				yearOccs=yearOccu.get(record.get(2));
				yO.add(record.get(5)); //timepoint
				yO.add(record.get(6)); //occurrence of timepoint
				
				if(normalizationType==2){
					yO.add(record.get(7)); //local normalization
				}
				
				
				yearOccs.add(yO);

				yearOccu.put(record.get(2),yearOccs);


				res.put(record.get(0), yearOccu);
			 }
		}
		else{
				
			//global normalization 
			if (normalizationType==3){
				fileNormalized = globalNormalizationFrequency(file);
			}
			//chi-square normalization
			else if(normalizationType==4){
				fileNormalized = chiNormalizationFrequency(file);
			}
		
			for (List<String> record:fileNormalized){
			
				ArrayList<String> yO = new ArrayList<String>();
				//System.out.println(record.get(0)+" "+record.get(2));
				if (!res.containsKey(record.get(0))){
					yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
					res.put(record.get(0), yearOccu);
				}
			
				yearOccu=res.get(record.get(0));
				if(!yearOccu.containsKey(record.get(2))){
				
					yearOccs = new HashSet<ArrayList<String>>();
					yearOccu.put(record.get(2),yearOccs);
				}
			
				yearOccs=yearOccu.get(record.get(2));
				yO.add(record.get(5)); //timepoint
				yO.add(record.get(6)); //occurrence of timepoint

				yearOccs.add(yO);
				yearOccu.put(record.get(2),yearOccs);

				res.put(record.get(0), yearOccu);
			}
		}
		
	
		/*for ( String str: res.keySet()){
			HashMap<String, HashSet<ArrayList<String>>> hm = res.get(str);
			for (String obj: hm.keySet()){
				System.out.println(str+" "+ obj+" "+ hm.get(obj));
			}
		}
		*/
		return res;
	}
	
	//global normalization function 
	public HashSet<List<String>> globalNormalizationFrequency(HashSet<ArrayList<String>> file){
		HashSet<List<String>> factsGN = new HashSet<List<String>>();

		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
		res=groupBySubjectObject(file);

		for (String u: res.keySet()){
			List<Fact> facts = new ArrayList<Fact>();
			List<Fact> factsGNs = new ArrayList<Fact>();
			HashMap<String,HashSet<ArrayList<String>>> objgrouping= res.get(u);
			
			for (String o: objgrouping.keySet()){
				for (List<String> f:objgrouping.get(o)){
					
					Fact fact = FactReader.readFact(f);

					facts.add(fact);

		        }
			}
			

			factsGNs = (new GlobalNormalization()).normalize(facts);
		
			for (Fact f:factsGNs){
				
				factsGN.add(FactReader.listFromFact(f));
	        }
			
		}
		/*for (List<String> f:factsGN){
			System.out.println(f);
		}*/
		
		
		return factsGN;
	}
	
	public HashSet<List<String>> chiNormalizationFrequency(HashSet<ArrayList<String>> file){
		HashSet<List<String>> factsGN = new HashSet<List<String>>();

		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
		res=groupBySubjectObject(file);

		for (String u: res.keySet()){
			List<Fact> facts = new ArrayList<Fact>();
			List<Fact> factsGNs = new ArrayList<Fact>();
			HashMap<String,HashSet<ArrayList<String>>> objgrouping= res.get(u);
			
			for (String o: objgrouping.keySet()){
			
				for (List<String> f:objgrouping.get(o)){
					
					Fact fact = FactReader.readFact(f);
			        facts.add(fact);

		        }
			}

			factsGNs = (new ChiSquaredGlobalNormalization()).normalize(facts);
		
			for (Fact f:factsGNs){
				factsGN.add(FactReader.listFromFact(f));
	        }
			
		}
		/*
		for (List<String> f:factsGN){
			System.out.println(f);
		}*/
		return factsGN;
	}
	
	public HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupBySubjectObject(HashSet<ArrayList<String>> file){
		
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	
		HashMap<String,HashSet<ArrayList<String>>> groupByObj = new HashMap<String,HashSet<ArrayList<String>>>();
		
		HashSet<ArrayList<String>> facts = new HashSet<ArrayList<String>>();
		
		for (ArrayList<String> f:file){
			
			if (!res.containsKey(f.get(0))){

					groupByObj = new HashMap<String,HashSet<ArrayList<String>>>();
					res.put(f.get(0), groupByObj);

			}
			
			groupByObj = res.get(f.get(0));
			if(!groupByObj.containsKey(f.get(2))){
				
				facts = new HashSet<ArrayList<String>>();
				groupByObj.put(f.get(2),facts);
			}
			
			facts=groupByObj.get(f.get(2));
			facts.add(f); 
			
			groupByObj.put(f.get(2),facts);

			res.put(f.get(0), groupByObj);
			
		 }
		
	
		/*for ( String str: res.keySet()){
			HashMap<String, HashSet<ArrayList<String>>> hm = res.get(str);
			for (String obj: hm.keySet()){
				System.out.println(str+" "+ obj+" "+ hm.get(obj));
			}
		}*/
		
		return res;
	}
	
	//normalization function
	public HashSet<List<String>> normalizeFrequency(HashSet<ArrayList<String>> file){
		HashSet<List<String>> normalizedFile = new HashSet<List<String>>();
		List<Fact> facts = new ArrayList<Fact>();
		
		for (List<String> f:file){
			Fact fact = FactReader.readFact(f);
			
	        facts.add(fact);

        }
        facts = (new GlobalNormalization()).normalize(facts);
			
       
        for (Fact f:facts){
        	normalizedFile.add(FactReader.listFromFact(f));
        }
		

		return normalizedFile;
	}

}
