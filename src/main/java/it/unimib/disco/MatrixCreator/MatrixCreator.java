package it.unimib.disco.MatrixCreator;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.Matching.Features;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.aksw.distributions.Fact;
import org.aksw.distributions.Fact.Entry;

import com.hp.hpl.jena.rdf.model.RDFNode;


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
	
		
	public HashMap<String, ArrayList<String>> fetchDatesTD(List<Fact> file){

		HashMap<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		ArrayList<String> years = new ArrayList<String>();
		

		Iterator<Fact> it = file.iterator();
		while (it.hasNext()){
			Fact f = new Fact();
			f = (Fact) it.next();
					
			if (!res.containsKey(f.get(Entry.SUBJECT))){
				years = new ArrayList<String>();
				res.put(f.get(Entry.SUBJECT), years);
			}
			
			years=res.get(f.get(Entry.SUBJECT));
			if (stringToLong(f.get(Entry.DATE)).before(stringToLong("2013-12-31"))&&stringToLong(f.get(Entry.DATE)).after(stringToLong("1980-01-01"))){
				if(!years.contains(f.get(Entry.DATE))){
					
					years.add(f.get(Entry.DATE));
				}
	
				res.put(f.get(Entry.SUBJECT), years);
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
	
	public HashMap<String,DateOccurrence [][]> createLexicalRIM(HashMap<String,List<Features>> featuresUri){
		HashMap<String, DateOccurrence [][]> maximalMatrix = new HashMap<String, DateOccurrence [][]>(); 
		
		for (String uri: featuresUri.keySet()){
			List<Features> hm = featuresUri.get(uri);
			HashSet<String> dateList = new HashSet<String>();
			HashMap<String,HashSet<String>> timepointpredicate= new HashMap<String,HashSet<String>>(); 
			HashSet<String> predicates= new HashSet<String>();
			for (Features y: hm){
				dateList.add(y.getTimepoint());
				if (!timepointpredicate.containsKey(y.getTimepoint())){
					predicates= new HashSet<String>();
					timepointpredicate.put(y.getTimepoint(), predicates);
				}

				predicates = timepointpredicate.get(y.getTimepoint());
				predicates.add(stemPredicateSP(y.getPredicate()));
				timepointpredicate.put(y.getTimepoint(),predicates);
			}
			
			Map<String, HashSet<String>> sortedMap = new TreeMap<String, HashSet<String>>(timepointpredicate);


			maximalMatrix.put(uri, matrixLexicalCreator(sortedMap));
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

		matrix[0][0]= new DateOccurrence("[Start]/", "End");

		
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

		for (int i=1; i<startobjOccurrence.size()+1; i++){
			
			for(int j=1; j<endobjOccurrence.size()+1; j++){
				
				if (stringToLong(matrix[i][0].getDate()).after(stringToLong(matrix[0][j].getDate()))){

						matrix[i][j]=new DateOccurrence("","X");
				}else{

					matrix[i][j]=new DateOccurrence("","1");
					
				}
			}
		}
		
		return matrix;

	}
	
	public DateOccurrence[][] matrixLexicalCreator(Map<String,HashSet<String>> features){
		int leng=0;
		for (String s: features.keySet()){
			HashSet<String> hs=features.get(s);

			Iterator<String> it = hs.iterator();
			while (it.hasNext()){
				leng++;
			}
		}
		DateOccurrence matrix[][]= new DateOccurrence[leng+1][leng+1];

		matrix[0][0]= new DateOccurrence("[Start]/", "End");

		
		int l=1;
		for(String timepoint: features.keySet()){

			Iterator<String> it =features.get(timepoint).iterator();
			while (it.hasNext()){
				String predicate=(String) it.next();
				matrix[l][0] = new DateOccurrence(timepoint,predicate);
				l++;
			}
		}
		
		int m=1;
		for(String timepoint: features.keySet()){

			Iterator<String> it =features.get(timepoint).iterator();
			while (it.hasNext()){
				String predicate=(String) it.next();
		
				matrix[0][m]=new DateOccurrence(timepoint,predicate);
			
				m++;
			}
		}

		for (int i=1; i<leng+1; i++){
			
			for(int j=1; j<leng+1; j++){
				
				if (stringToLong(matrix[i][0].getDate()).after(stringToLong(matrix[0][j].getDate()))){

						matrix[i][j]=new DateOccurrence("","X");
				}else{

					matrix[i][j]=new DateOccurrence("","1");
					
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
