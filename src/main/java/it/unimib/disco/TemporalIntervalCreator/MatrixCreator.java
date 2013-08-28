package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aksw.distributions.Fact;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;


public class MatrixCreator {
	ReadFiles rf=new ReadFiles();

	public HashMap<String, HashMap<ArrayList<String>,Integer>> temporalPredicateExtractor(HashMap<String, HashMap<String, HashSet<String>>> repositoryDates,boolean predicate){
		HashMap<String, HashMap<ArrayList<String>,Integer>> resObjPre = new HashMap<String, HashMap<ArrayList<String>,Integer>>();
		
		for (String Uri: repositoryDates.keySet()){
			HashMap<ArrayList<String>,Integer> occurrenceDate=extractTemporalPredicate(repositoryDates.get(Uri),predicate);
			
			resObjPre.put(Uri, occurrenceDate);	
		}	
	
		return resObjPre;	
	}
	
	public HashSet<Fact> fetchtemporalfacts (HashMap<String,OntModel> models){
		
		HashSet<Fact> result = new HashSet<Fact>();
		
		for(String rUri: models.keySet()){

			OntModel m = models.get(rUri);
			
			StmtIterator iter = m.listStatements();
				
			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
				
			    Statement stmt      = iter.next();  // get next statement
			    Fact f = new Fact();

			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
 
			    RDFNode   object    = stmt.getObject();      // get the object

			    if (object instanceof Resource) {

			    } else {
			    	
			        // object is a literal
			    	if(verifyDate(object)){
			    		String dateStr= stemDate(object.toString());
			    		
			    		if (stringToLong(dateStr).before(stringToLong("2013-12-31"))){
			    			f.add(Fact.Entry.SUBJECT, rUri);
			    	        f.add(Fact.Entry.PREDICATE, predicate.toString());
			    	        f.add(Fact.Entry.OBJECT, dateStr);
			    	        result.add(f);
			    		}
			    	}
			    }
			    
			}
			/********add year 2013*******/
			Fact f = new Fact();
			f.add(Fact.Entry.SUBJECT, rUri);
			f.add(Fact.Entry.PREDICATE, "http://dbpedia.org/property/years");
			f.add(Fact.Entry.OBJECT, "2013");
			result.add(f);

		
		}
		return result;
	}
	
	
	public HashMap<String, HashMap<String, HashSet<String>>> fetchdateswithpredicates(HashMap<String,OntModel> models){
		
		HashMap<String, HashMap<String, HashSet<String>>> result = new HashMap<String, HashMap<String, HashSet<String>>>();
		
		for(String rUri: models.keySet()){
			
			HashMap<String, HashSet<String>> objPred = new HashMap<String,HashSet<String>>();

			OntModel m = models.get(rUri);
			
			StmtIterator iter = m.listStatements();
				
			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
				
			    Statement stmt      = iter.next();  // get next statement

			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
 
			    RDFNode   object    = stmt.getObject();      // get the object

			    if (object instanceof Resource) {

			    } else {
			    	
			        // object is a literal
			    	if(verifyDate(object)){
			    		String dateStr= stemDate(object.toString());
			    		
			    		if (stringToLong(dateStr).before(stringToLong("2013-12-31"))){
			    			if(!objPred.containsKey(dateStr)){
						    		
			    				HashSet<String> pred= new HashSet<String>();
						    	objPred.put(dateStr, pred);
						    }
		
			    			HashSet<String> pred= objPred.get(dateStr);
							pred.add(predicate.toString());
			    		}
			    	}
			    }
			}
			if(!objPred.containsKey("2013")){
	    		
    			HashSet<String> pred= new HashSet<String>();
    			objPred.put("2013", pred);
    		}
			
			HashSet<String> pred= objPred.get("2013");
			pred.add("http://dbpedia.org/property/years");
			result.put(rUri, objPred);
		
		}
		return result;
	}
	
	
	public HashMap<String, HashMap<String, HashSet<String>>> fetchDatesTD(List<String> temporaldefacto){
		
		HashSet<ArrayList<String>> file=rf.readCommaSeparatedFile(temporaldefacto);
		HashMap<String, HashMap<String, HashSet<String>>> res = new HashMap<String, HashMap<String, HashSet<String>>>();
		HashMap<String,HashSet<String>> dateObject = new HashMap<String,HashSet<String>>();
		HashSet<String> object = new HashSet<String>();
		
		for (List<String> record:file){
					
			if (!res.containsKey(record.get(0))){
				dateObject = new HashMap<String,HashSet<String>>();
				res.put(record.get(0), dateObject);
			}
			
			dateObject=res.get(record.get(0));
			if (stringToLong(record.get(5)).before(stringToLong("2013-12-31"))&&stringToLong(record.get(5)).after(stringToLong("1980-01-01"))){
				if(!dateObject.containsKey(record.get(5))){
					
					object = new HashSet<String>();
					dateObject.put(record.get(5),object);
				}
				
				object=dateObject.get(record.get(5));
				object.add(record.get(2)); //object	
	
				dateObject.put(record.get(5),object);
	
	
				res.put(record.get(0), dateObject);
			}
		}

		return res;
	}
	
	
	public HashMap<String,DateOccurrence [][]> createMaximalRIM(HashMap<String,ArrayList<String>> repositoryDates){
		HashMap<String, DateOccurrence [][]> maximalMatrix = new HashMap<String, DateOccurrence [][]>(); 
		
		for (String uri: repositoryDates.keySet()){
			
			ArrayList<String> dates = new ArrayList<String>();
			dates = repositoryDates.get(uri);

			Collections.sort(dates);

			maximalMatrix.put(uri, matrixCreator(dates,dates));
		}
		
		return maximalMatrix;		
		
	}
	
	public HashMap<ArrayList<String>,Integer> extractTemporalPredicate(HashMap<String, HashSet<String>> objPred, boolean predSE){
		
		HashMap<ArrayList<String>,Integer> timePoints = new HashMap<ArrayList<String>,Integer>();
		
		for (String date: objPred.keySet()){
			int i=0;
			for(String pred:objPred.get(date)){
				ArrayList<String> obpred = new ArrayList<String>();
				
				obpred.add(stemDate(date));
				if (predSE){
					obpred.add(stemPredicateSE(pred));
				}
				else{
					obpred.add(stemPredicateSE(pred));
					obpred.add(stemPredicateSP(pred));
				}
				
				//count the occurrence of the pair object-predicate
				if(!timePoints.containsKey(obpred)){

	    			timePoints.put(obpred, 1);
	    		}
				else{
					i=timePoints.get(obpred)+1;
					timePoints.put(obpred, i);
				}
			}

		}
		timePoints=ordering(timePoints, predSE);
		return timePoints;
	}
	
	
	
	public DateOccurrence[][] matrixCreator(ArrayList<String> startobjOccurrence, ArrayList<String> endobjOccurrence){
		DateOccurrence matrix[][]= new DateOccurrence[startobjOccurrence.size()+1][endobjOccurrence.size()+1];
		ArrayList<String> occurrence = new ArrayList<String>();
		occurrence.add("End");
		matrix[0][0]= new DateOccurrence("[Start]/", occurrence);

		
		int l=1;
		for(String startobj: startobjOccurrence){
			matrix[l][0] = new DateOccurrence(startobj);
			l++;
		}
		
		int m=1;
		for(String endobj: endobjOccurrence){
			matrix[0][m]=new DateOccurrence(endobj);
			
			m++;
		}

		occurrence = new ArrayList<String>();
		occurrence.add("X");
		for (int i=1; i<startobjOccurrence.size()+1; i++){
			
			for(int j=1; j<endobjOccurrence.size()+1; j++){
				
				if (stringToLong(matrix[i][0].getDate()).after(stringToLong(matrix[0][j].getDate()))){
					occurrence = new ArrayList<String>();
					occurrence.add("X");
						matrix[i][j]=new DateOccurrence("",occurrence);
				}else{
					occurrence = new ArrayList<String>();
					occurrence.add("1");
					matrix[i][j]=new DateOccurrence("",occurrence);
					
				}
			}
		}
		
		return matrix;

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
	
	
	public String stemDate (String date) {
		
		//Stemming of the string date
		
		if (date.contains("-")){
			date=date.substring(0,date.indexOf("-"));
		}
		else if (date.contains("/")){
			date=date.substring(0,date.indexOf("/"));
		}
		else{}
		if (date.contains("^")){
			date=date.substring(0,date.indexOf('^'));
		}
		else{}
		if(date.contains("T")){
			date=date.substring(0, date.indexOf('T'));
		}
		else{}
		if(date.contains(".")){
			date = date.substring(0,date.indexOf('.'));	
		}
		else{}
		
	    return date;

	}
	
	public String stemPredicateSE (String predicate) {
		String symb; 
	    String str = predicate; 
	    
	    //Stemming of the string
	    str=str.toLowerCase().substring(str.lastIndexOf('/')+1);
	    
	    if(str.contains("start")||str.contains("begin")||str.contains("birth")) {
	    	symb="start";
	    }
	    else if (str.contains("end")||str.contains("death")) {
	    	symb="end";
	    }
	    else{
	    	symb="NA";
	    }
		
	    return symb;

	}
	
	public String stemPredicateSP (String predicate) {
		String symb1="",symb2="",symb=""; 
	    String str = predicate; 
	    int start=0;
	    //Stemming of the string
	    str=str.toLowerCase().substring(str.lastIndexOf('/')+1);

	    
       if (str.contains("start")){  
            	start=str.indexOf("start");
            	if (start>0){
            		symb1=str.substring(0,start);
            		if ((start+5)>0){
            			symb2=str.substring(start+5);
            		}
            	}
            	else{
            		symb2=str.substring(start+5);
            	}
	    		symb=symb1+symb2;

       }
       else if(str.contains("begin")){  
            	start = str.indexOf("begin");
            	if (start>0){
            		symb1=str.substring(0,start);
            		if ((start+5)>0){
            			symb2=str.substring(start+5);
            		}
            	}
            	else{
            		symb2=str.substring(start+5);
            	}
            	symb=symb1+symb2;

       }
       else if(str.contains("birth")){  
            	start = str.indexOf("birth");
            	if (start>0){
            		symb1=str.substring(0,start);
            		if ((start+5)>0){
            			symb2=str.substring(start+5);
            		}
            	}
            	else{
            		symb2=str.substring(start+5);
            	}
            	symb=symb1+symb2;

       }
       else if(str.contains("end")){  
            	start = str.indexOf("end");
            	if (start>0){
            		symb1=str.substring(0,start);
            		if ((start+5)>0){
            			symb2=str.substring(start+3);
            		}
            	}
            	else{
            		symb2=str.substring(start+5);
            	}
            	symb=symb1+symb2;

       }
       else if(str.contains("death")){  
            	start = str.indexOf("death");
            	if (start>0){
            		symb1=str.substring(0,start);
            		if ((start+5)>0){
            			symb2=str.substring(start+5);
            		}
            	}
            	else{
            		symb2=str.substring(start+5);
            	}
            	symb=symb1+symb2;

       }
       else{symb="NA";}
       return symb;

	}
	
	public boolean verifyDate(RDFNode dateObject){
		boolean verified = false;
		if ((dateObject.toString().matches("(^(((19|20)\\d\\d)[-/\\.](0?[1-9]|1[012])[-/\\.](0?[1-9]|[12][0-9]|3[01]).*))"))
				||(dateObject.toString().matches("(^(((19|20)\\d\\d)).*)"))||
				(dateObject.toString().matches("(^(((19|20)\\d\\d)[-/\\.](0?[1-9]|1[012]).*))"))
				){
			verified=true;
		}
		
		return verified;

	}
	
	@SuppressWarnings("unchecked")
	public HashMap<ArrayList<String>,Integer> ordering(HashMap<ArrayList<String>,Integer> hashMap, boolean predSE){
		
		HashMap<ArrayList<String>,Integer> orderedHashMap = new HashMap<ArrayList<String>,Integer>();
		
		ArrayList<String> al= new ArrayList<String>();
		List<String> hs= new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Map sortedMap = new LinkedHashMap();
		
		for (ArrayList<String> datePred: hashMap.keySet()){
			hs.add(datePred.get(0));
		}
		Collections.sort(hs);
		
		
		for (String str : hs){
			for (ArrayList<String> datePred: hashMap.keySet()){
				
				al= new ArrayList<String>();
				if(str.equals(datePred.get(0))){

					al.add(datePred.get(0));
					if (predSE){
						al.add(datePred.get(1));
					}
					else {
						al.add(datePred.get(1));
						al.add(datePred.get(2));
					}
					
					sortedMap.put(al, hashMap.get(al));
				}
			}
		}
		orderedHashMap=(HashMap<ArrayList<String>, Integer>) sortedMap;

		return orderedHashMap;
	}
}
