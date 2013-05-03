package it.unimib.disco.FactExtractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class FactExtractor {

	private static Logger logger = Logger.getLogger(TemporalFactAnnotator.class);
	
	public HashMap<String, ArrayList<String>> fetchFacts(HashMap<String,OntModel> models){
		
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		
		for(String rUri: models.keySet()){
			
			ArrayList<String> predObj = new ArrayList<String>();
			
			OntModel m = models.get(rUri);
			StmtIterator iter = m.listStatements();

			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
			    Statement stmt      = iter.nextStatement();  // get next statement
			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object
			    
			    if (object instanceof Resource) {
			    	predObj.add(0, predicate.toString());
					predObj.add(1, object.toString());
			    } else {
			        // object is a literal
			    	if(!verifyDate(object)){
			    		predObj.add(0, predicate.toString());
						predObj.add(1, object.toString());
			    	}
			    	
			    }
			} 
						
			result.put(rUri, predObj);
		}
		return result;
	}
	

	public HashMap<String, HashMap<ArrayList<String>, String>> fetchDates(HashMap<String,OntModel> models){
		
		HashMap<String, HashMap<ArrayList<String>, String>> result = new HashMap<String, HashMap<ArrayList<String>, String>>();
		
		for(String rUri: models.keySet()){
			HashMap<ArrayList<String>, String> predObj = new HashMap<ArrayList<String>, String>();
			
			
			OntModel m = models.get(rUri);
			// list the statements in the Model
			StmtIterator iter = m.listStatements();

			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
				ArrayList<String> predObjKey= new ArrayList<String>();
			    Statement stmt      = iter.nextStatement();  // get next statement
			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object
			    
			    if (object instanceof Resource) {

			    } else {
			        // object is a literal
			    	if(verifyDate(object)){
			    		predObjKey.add(0, predicate.toString());
			    		predObjKey.add(1, object.toString());
			    		predObj.put(predObjKey, object.toString());
			    		logger.info("Retrieved property value: "+ object.toString());
			    	}
			    }
			} 
			result.put(rUri, predObj);
		}
		return result;
	}
	
public HashSet<String> fetchReducedUriList(HashMap<String,OntModel> models){
		
	HashSet<String> result = new HashSet<String>();
		
		for(String rUri: models.keySet()){

			OntModel m = models.get(rUri);
			// list the statements in the Model
			StmtIterator iter = m.listStatements();

			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
				
			    Statement stmt      = iter.nextStatement();  // get next statement
			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object
			    
			    if (!predicate.toString().contains("birth")) {

			    }
			    else{
			    	if(verifyDate(object)){
			    		if(stringToLong(object.toString()).after(stringToLong("1983-01-01"))){
			    			result.add(rUri);
			    		}
			    	}
			    		
			   }
			}
			System.gc();
		} 
		
		return result;
	}

public HashSet<ArrayList<String>> fetchReducedFactList(HashMap<String,OntModel> models, HashSet<ArrayList<String>> factYago ){
	
	HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
	
	for(String rUri: models.keySet()){
		
		ArrayList<String> fact = new ArrayList<String>();
		
		OntModel m = models.get(rUri);
		StmtIterator iter = m.listStatements();

		while (iter.hasNext()) {
			
		    Statement stmt      = iter.nextStatement();  // get next statement
		   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object
		    
		    if (!predicate.toString().contains("birth")) {

		    }
		    else{
		    	//System.out.println(rUri+" "+stmt);
		    	if(verifyDate(object)){
		    		if(stringToLong(object.toString()).after(stringToLong("1983-01-01"))){
		    			
		    			for (ArrayList<String> ar:factYago){
		    				if(ar.get(0).contains(rUri)){
		    					fact=ar;
		    					//System.out.println(fact);
		    				}
		    				
		    			}
		    		}
		    	}
		    		
		   }
		}
		System.gc();			
		result.add(fact);
	}
	return result;
	}

public Date stringToLong(String datestr){
	Date date = null;
	SimpleDateFormat[] FORMATS = {
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyy-MM"),
			new SimpleDateFormat("yyyy")
			};
	for (DateFormat formatter : FORMATS) {
        try {	
        	
        	date = formatter.parse(datestr);
			break;
        
        } catch (java.text.ParseException ex) {
			continue;
        }
    }

	return date;
	
}
	
	public boolean verifyDate(RDFNode dateObject){
		boolean verified = false;
		if ((dateObject.toString().matches("(^(((19|20)\\d\\d)[-/\\.](0?[1-9]|1[012])[-/\\.](0?[1-9]|[12][0-9]|3[01]).*))"))){
			verified=true;
		}
		
		return verified;

	}
}
