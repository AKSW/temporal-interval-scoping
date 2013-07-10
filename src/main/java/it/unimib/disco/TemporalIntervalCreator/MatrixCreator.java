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
import java.util.TreeMap;

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
			HashMap<ArrayList<String>,Integer> occurrenceDate=infoTemporalPredicate(repositoryDates.get(Uri),predicate);
			
			resObjPre.put(Uri, occurrenceDate);	
		}	
	
		return resObjPre;	
	}
	
	public HashMap<String, HashMap<String, HashSet<String>>> fetchDates(HashMap<String,OntModel> models){
		
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
//System.out.println(predicate);
			    RDFNode   object    = stmt.getObject();      // get the object

			    if (object instanceof Resource) {
		    	
			    	
			    } else {
			    	
			        // object is a literal
			    	if(verifyDate(object)){
			    		String dateStr = object.toString().substring(0,object.toString().indexOf('.'));

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
	
	
	public HashMap<String,DateOccurrence [][]> maximalMatrixCreator(HashMap<String, HashMap<String, HashSet<String>>> repositoryDates){
		HashMap<String, DateOccurrence [][]> maximalMatrix = new HashMap<String, DateOccurrence [][]>(); 
		for (String Uri: repositoryDates.keySet()){
		
			HashMap<String, HashSet<String>> objPred = repositoryDates.get(Uri);
			HashMap<String, ArrayList<String>> objOccurrence = new HashMap<String, ArrayList<String>>();
			
			for(String obj:objPred.keySet()){
				ArrayList<String> occurrence= new ArrayList<String>();
				int occ = 0;
				for (@SuppressWarnings("unused") String pred: objPred.get(obj)){
					occ++;
				}
				
				occurrence.add(Integer.toString(occ));
				objOccurrence.put(stemDate(obj), occurrence);
				/*if (stringToLong(stemDate(obj)).before(stringToLong("2013-12-31"))){
					objOccurrence.put(stemDate(obj), occurrence);
				}*/
			}

			Map<String, ArrayList<String>> sortedMap = new TreeMap<String, ArrayList<String>>(objOccurrence);

			maximalMatrix.put(Uri, matrixCreator(sortedMap,sortedMap));
		}
		
		return maximalMatrix;		
		
	}
	
	public HashMap<ArrayList<String>,Integer> infoTemporalPredicate(HashMap<String, HashSet<String>> objPred, boolean predSE){
		
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
	
	public HashMap<String,DateOccurrence [][]> matrixReducerSE(HashMap<String, HashMap<ArrayList<String>,Integer>> timeAnnotated, 
			HashMap<String, DateOccurrence [][]> maximalMatrixDates){
		
		HashMap<String,DateOccurrence [][]> reducedMatrix = new HashMap<String,DateOccurrence [][]>();
	
		for (String Uri: maximalMatrixDates.keySet()){
			DateOccurrence [][] matrix = maximalMatrixDates.get(Uri);
			
			ArrayList<String> occurrence = new ArrayList<String>();
			
			for(int i=0; i<matrix.length;i++){
				
				for(int j=0; j<matrix[i].length; j++){
				
					for (ArrayList<String> objpred: timeAnnotated.get(Uri).keySet()){

							String occu = Integer.toString(timeAnnotated.get(Uri).get(objpred));
							
							if (matrix[i][0].getDate().equals(objpred.get(0))){
								
								if (objpred.get(1).equals("end")){
									if(occu.equals(matrix[i][0].getOccurrence().get(0))){
										
										if (j==0){
											if ((matrix[i][j].getPred()!=null)&&(matrix[i][j].getPred().equals("start"))){}
											else{
												matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),objpred.get(1),matrix[i][j].getOccurrence());
											}
											
										}
										else{
											if(!(matrix[i][0].getPred().equals("start"))){
												occurrence = new ArrayList<String>();
												occurrence.add("X");
												matrix[i][j]=new DateOccurrence("",occurrence);
											}
										}
										
									}
									else{}
							
								}
							
								else if ((objpred.get(1).equals("start")))

									if(occu.equals(matrix[i][0].getOccurrence().get(0))){
										
										matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),objpred.get(1),matrix[i][j].getOccurrence());
									}

									else{
										occurrence = new ArrayList<String>();
										occurrence.add(occu);
										if (j==0){
											if ((matrix[i][j].getPred()!=null)&&(matrix[i][j].getPred().equals("start"))){}
											else{
												matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),objpred.get(1),occurrence);
											}

										}
										else{
											matrix[i][j]=new DateOccurrence("",matrix[i][j].getOccurrence());
										}
										
									}
							}			
							else{}
						}
				}
			}
			
			for(int i=0; i<matrix.length;i++){
				
				for(int j=0; j<matrix[i].length; j++){
				
					for (ArrayList<String> objpred: timeAnnotated.get(Uri).keySet()){
							String occu = Integer.toString(timeAnnotated.get(Uri).get(objpred));
							if(matrix[0][j].getDate().equals(objpred.get(0))){
				
								if (objpred.get(1).equals("start")){
												
									if(occu.equals(matrix[0][j].getOccurrence().get(0))){

										if (i==0){
											if ((matrix[i][j].getPred()!=null)&&(matrix[i][j].getPred().equals("end"))){}
											else{
												matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),objpred.get(1),matrix[i][j].getOccurrence());
											}
										}
										else{
											if(!(matrix[0][j].getPred().equals("end"))){
												occurrence = new ArrayList<String>();
												occurrence.add("X");
												matrix[i][j]=new DateOccurrence("",occurrence);
											}
										}
									}
				
								}
								else if ((objpred.get(1).equals("end"))){
									if(occu.equals(matrix[i][0].getOccurrence().get(0))){
						
										matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),matrix[i][j].getOccurrence());
									}
					
									else{
										occurrence = new ArrayList<String>();
										occurrence.add(occu);
										if (i==0){
											if ((matrix[i][j].getPred()!=null)&&(matrix[i][j].getPred().equals("end"))){}
											else{
												matrix[i][j]=new DateOccurrence(matrix[i][j].getDate(),objpred.get(1),occurrence);
											}

										}
										else{
											matrix[i][j]=new DateOccurrence("",matrix[i][j].getOccurrence());
										}
									}
								}
								else{}
							}}}}
			
			
			reducedMatrix.put(Uri, matrix);
		}
		return reducedMatrix; 
	}
	
	
	public HashMap<String,DateOccurrence [][]> matrixReducerSP(HashMap<String, HashMap<ArrayList<String>,Integer>> timeAnnotated, 
			HashMap<String, DateOccurrence [][]> reducedMatrix1){
		
		HashMap<String,DateOccurrence [][]> reducedMatrix = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();
		
		for (String Uri: reducedMatrix1.keySet()){
			DateOccurrence [][] reduMatrix= reducedMatrix1.get(Uri);
			
			Map<ArrayList<String>, Integer> objpredoccu = new LinkedHashMap<ArrayList<String>,Integer>();

		for (ArrayList<String> str : timeAnnotated.get(Uri).keySet() ){
				objpredoccu.put(str, timeAnnotated.get(Uri).get(str));
		}
		
		DateOccurrence [][] rMatrix = matrixCreator2(objpredoccu,objpredoccu,reduMatrix);
		
			for (int i=1; i<rMatrix.length; i++){
				for(int j=1; j<rMatrix[i].length; j++){
					
					if (!(rMatrix[i][j].getOccurrence().get(0).equals("X"))){
					
							if (!(rMatrix[0][j].getOccurrence().get(1).equals(rMatrix[i][0].getOccurrence().get(1)))){
		
								occurrence = new ArrayList<String>();
								occurrence.add("X");
								
								rMatrix[i][j]= new DateOccurrence("", occurrence);
							}
					}
				}
			}
				
			reducedMatrix.put(Uri, rMatrix);
		}
		return reducedMatrix;
	}
	
	public DateOccurrence[][] matrixCreator(Map<String, ArrayList<String>> startobjOccurrence, Map<String, ArrayList<String>> endobjOccurrence){
		DateOccurrence matrix[][]= new DateOccurrence[startobjOccurrence.size()+1][endobjOccurrence.size()+1];
		ArrayList<String> occurrence = new ArrayList<String>();
		occurrence.add("End");
		matrix[0][0]= new DateOccurrence("[Start]/", occurrence);

		
		int l=1;
		for(String startobj: startobjOccurrence.keySet()){
			matrix[l][0] = new DateOccurrence(startobj,startobjOccurrence.get(startobj));
			l++;
		}
		
		int m=1;
		for(String endobj: endobjOccurrence.keySet()){
			matrix[0][m]=new DateOccurrence(endobj,endobjOccurrence.get(endobj));
			
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
	
	public DateOccurrence[][] matrixCreator2(Map<ArrayList<String>,Integer> startobjOccurrence, Map<ArrayList<String>,Integer> endobjOccurrence, DateOccurrence [][] reduMatrix){
		
		int column=endobjOccurrence.size();
		int row = endobjOccurrence.size();
		/*for(ArrayList<String> startobj: endobjOccurrence.keySet()){
			
			if (startobj.get(1).equals("start")||startobj.get(1).equals("NA")){
				row++;
			}
		}
		for(ArrayList<String> endobj: endobjOccurrence.keySet()){

			if (endobj.get(1).equals("end")||endobj.get(1).equals("NA")){
				column++;
			}
		}*/
		
		
		DateOccurrence matrix[][]= new DateOccurrence[row+1][column+1];
		
		ArrayList<String> occurrence = new ArrayList<String>();
		occurrence.add("End");
		matrix[0][0]= new DateOccurrence("[Start]/", occurrence);
		
		int l=1;
		for(ArrayList<String> startobj: startobjOccurrence.keySet()){
			occurrence = new ArrayList<String>();
			//if (startobj.get(1).equals("start")||startobj.get(1).equals("NA")){
				occurrence.add(startobj.get(1));
				occurrence.add(startobj.get(2));
				occurrence.add(String.valueOf(startobjOccurrence.get(startobj)));
				matrix[l][0] = new DateOccurrence(startobj.get(0),occurrence);
				l++;
			//}
		}
		
		int m=1;
		for(ArrayList<String> endobj: endobjOccurrence.keySet()){
			occurrence = new ArrayList<String>();
			//if (endobj.get(1).equals("end")||endobj.get(1).equals("NA")){
			occurrence.add(endobj.get(1));
			occurrence.add(endobj.get(2));
			occurrence.add(String.valueOf(startobjOccurrence.get(endobj)));
			matrix[0][m] = new DateOccurrence(endobj.get(0),occurrence);
		
			m++;
			//}
		}

		occurrence = new ArrayList<String>();
		occurrence.add("X");
		int q, j, p=1,i=1;
		
		while (i<row+1){
			q=1;j=1;
			while(j<column+1){
				
				if (stringToLong(matrix[0][j].getDate()).equals(stringToLong(reduMatrix[0][q].getDate()))){
					if(stringToLong(matrix[i][0].getDate()).equals(stringToLong(reduMatrix[p][0].getDate()))){

						matrix[i][j]=new DateOccurrence("",reduMatrix[p][q].getOccurrence());
						j++;
					}else{
						p++;
					}
					
				}
				else{
					q++;
				}
			}
			i++;
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
		
		if (date.contains("^")){
			date=date.substring(0,date.indexOf('^'));
			if(date.contains("T")){
				date=date.substring(0, date.indexOf('T'));
			}
			else{}
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
