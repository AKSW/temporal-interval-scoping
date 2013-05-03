package it.unimib.disco.YagoDBpediaMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class YagoDBpediaMapper {

	public String subjectYago, predicateYago, objectYago,predicateYagoTemp,pobjectYagoTemp;
	public HashMap<String, HashSet<String>> predList = new HashMap<String,HashSet<String>>();
	
	HashMap<String, HashSet<String>> fetchPredicates(OntModel model, ArrayList<String> yagoFactsDBp){
		
		
		subjectYago = yagoFactsDBp.get(0);
		predicateYago = yagoFactsDBp.get(1);
		objectYago = yagoFactsDBp.get(2);

		// list the statements in the Model
		StmtIterator iter = model.listStatements();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
				
			Statement stmt      = iter.nextStatement();  // get next statement
			// Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			Property  predicate = stmt.getPredicate();   // get the predicate
			RDFNode   object    = stmt.getObject();      // get the object
			if(predicateYago.contains("created")){System.out.println(predicateYago);
			System.out.println(objectYago+" "+object.toString());}
			if(objectYago.equalsIgnoreCase(object.toString())){
				
				if(!predList.containsKey(predicateYago)){
			    	HashSet<String> pred= new HashSet<String>();
			    	predList.put(predicateYago, pred);
			    }

			   HashSet<String> pred= predList.get(predicateYago);
			   pred.add(predicate.toString());
			   
			}
			
		} 
		return predList;
	}
	
	public HashSet<String> fetchDBpediaFacts(HashSet<ArrayList<String>> factYago, HashMap<String, HashSet<String>> repositoryPredicates){
		
		HashSet <String> allFacts= new HashSet<String>(); 
		
		for (ArrayList<String> yagoFact: factYago){
			
			subjectYago = yagoFact.get(0);
			predicateYago = yagoFact.get(1);
			objectYago = yagoFact.get(2);
			predicateYagoTemp = yagoFact.get(3);
			pobjectYagoTemp = yagoFact.get(4);
	
			for(String str:repositoryPredicates.keySet()){
				if (predicateYago.equalsIgnoreCase(str)){
					for (String dbpediaProp: repositoryPredicates.get(str)){
						
						String fact=subjectYago+", "+dbpediaProp+", "+objectYago+", "+predicateYagoTemp+", "+pobjectYagoTemp;
						
						allFacts.add(fact);
					}
				}
			}
		}
		return allFacts;
	}
}
