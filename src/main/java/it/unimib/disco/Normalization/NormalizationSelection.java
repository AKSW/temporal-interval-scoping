package it.unimib.disco.Normalization;

import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aksw.distributions.ChiSquaredGlobalNormalization;
import org.aksw.distributions.Fact;
import org.aksw.distributions.GlobalNormalization;
import org.aksw.distributions.LocalNormalization;

public class NormalizationSelection {

	ReadFiles rf=new ReadFiles();	
	FactGrouping group=new FactGrouping();
	
	public List<Fact> normalize(int normalizationType, List<Fact> l){
				
		List<Fact> fileNormalized= new ArrayList<Fact>();
		
		//no-normalization
		if(normalizationType == 1){
			for (int i = 0; i < l.size(); i++) {
				fileNormalized.add(l.get(i));
	        }
		}
		
		else{
				
			//local normalization 
			if (normalizationType==2){
				fileNormalized = localNormalizationFrequency(l);
			}
			//global normalization 
			else if (normalizationType==3){
				fileNormalized = globalNormalizationFrequency(l);
			}
			//chi-square normalization
			else if(normalizationType==4){
				fileNormalized = chiNormalizationFrequency(l);
			}
		
		}
		
		/*for(int i=0;i<fileNormalized.size();i++){
			System.out.println(fileNormalized.get(i).toString());
		}*/
		
		return fileNormalized;
	}
	
	//local normalization function
	public List<Fact> localNormalizationFrequency(List<Fact> listOfFacts){
		List<Fact> factsGNs = new ArrayList<Fact>();
		List<Fact> copy = new ArrayList<Fact>();  
		
		HashMap<String,HashMap<String,List<Fact>>> res = new HashMap<String,HashMap<String,List<Fact>>>();
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
	
	//global normalization function 
	public List<Fact> globalNormalizationFrequency(List<Fact> listOfFacts){
		List<Fact> factsGNs = new ArrayList<Fact>();
		List<Fact> copy = new ArrayList<Fact>(); 
		
		HashMap<String,HashMap<String,List<Fact>>> res = new HashMap<String,HashMap<String,List<Fact>>>();
		res=group.groupBySubjectObject(listOfFacts);
		
		
		for (String u: res.keySet()){
			List<Fact> facts = new ArrayList<Fact>();
			HashMap<String,List<Fact>> objgrouping= res.get(u);
			
			for (String o: objgrouping.keySet()){
				for (Fact f:objgrouping.get(o)){

					facts.add(f);
		        }
			}
			
			factsGNs = (new GlobalNormalization()).normalize(facts);
		
			 for (int i = 0; i < factsGNs.size(); i++) {
		            copy.add(factsGNs.get(i));
		      }
			
		}
		/*for (Fact f:copy){
			System.out.println(f);
		}*/
		
		
		return copy;
	}
	
	public List<Fact> chiNormalizationFrequency(List<Fact> listOfFacts){
		List<Fact> factsGNs = new ArrayList<Fact>();
		List<Fact> copy = new ArrayList<Fact>();

		HashMap<String,HashMap<String,List<Fact>>> res = new HashMap<String,HashMap<String,List<Fact>>>();
		res=group.groupBySubjectObject(listOfFacts);

		for (String u: res.keySet()){
			List<Fact> facts = new ArrayList<Fact>();
			
			HashMap<String,List<Fact>> objgrouping= res.get(u);
			
			for (String o: objgrouping.keySet()){
			
				for (Fact f: objgrouping.get(o)){
					
			        facts.add(f);

		        }
			}

			factsGNs = (new ChiSquaredGlobalNormalization()).normalize(facts);
		
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
