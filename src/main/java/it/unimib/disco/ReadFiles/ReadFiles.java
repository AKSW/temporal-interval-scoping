package it.unimib.disco.ReadFiles;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.MatrixCreator.MatrixCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.aksw.distributions.Fact;
import org.aksw.distributions.FactReader;

public class ReadFiles {

	public static List<String> getURIs(File rsListFile) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(rsListFile));
			
			String line = br.readLine();
			
			while(line!=null){
				
				if(line.charAt(0)!='#'){
					list.add(line);
				}
				line=br.readLine();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;

	}
	
	public HashSet<ArrayList<String>> readCommaSeparatedFile(List<String> file) {
		HashSet<ArrayList<String>> fileArray = new HashSet<ArrayList<String>>(); 
		
		
		for(String record:file){
			record=record.trim();
			ArrayList<String> list = new ArrayList<String>();
			String subj=record.substring(0,record.indexOf(';'));
			list.add(subj);
			String remainingRecord= record.substring(record.indexOf(';')+1);
			//remainingRecord=remainingRecord.substring(remainingRecord.indexOf('[')+1,remainingRecord.indexOf(']'));
			
			int begin;
			do{
				
				begin= remainingRecord.indexOf(';');
				
				if(begin<0){
					
					list.add(remainingRecord.substring(0,remainingRecord.length()));
				}
				else{
					list.add(remainingRecord.substring(0, begin));

					remainingRecord=remainingRecord.substring(begin+1);
				}
				
			}while (begin>0);

			fileArray.add(list);
		}
		return fileArray;

	}
	
	public HashMap<String, HashMap<String, DateOccurrence[][]>> readTabSeparatedMatrixFile(File rsListMatrixFile)
	{
		HashMap<String, HashMap<String, DateOccurrence[][]>> subject_object_matrix = new HashMap<String, HashMap<String, DateOccurrence[][]>>();
		
		BufferedReader br = null;
		String line = "";
		String fileSplitBySpace = " ";
		String fileSplitByTab = "\\t";
		try 
		{
 
			br = new BufferedReader(new FileReader(rsListMatrixFile));
			line = br.readLine();
			while (line != null)
			{
				line = line.trim();
				String[] string_tokens = line.split(fileSplitBySpace);
				if (string_tokens.length == 2)
				{
					String subject = string_tokens[0];
					String object = string_tokens[1];
					DateOccurrence[][] matrix = new DateOccurrence[0][0];
					ArrayList<String> startendobjOccurrence = new ArrayList<String>();
					int rows_count = 0;
					
					line = br.readLine();
					if (line == null) throw new Exception("File not well formatted.");
					string_tokens = line.split(fileSplitByTab);
					if (string_tokens.length < 2) throw new Exception("File not well formatted.");
					rows_count = string_tokens.length; //rows count == col count
					for (int i = 1; i < rows_count; i++)
					{
						//check date format?
						startendobjOccurrence.add(string_tokens[i]);
					}
					matrix = new MatrixCreator().matrixCreator(startendobjOccurrence,startendobjOccurrence);
					for (int i = 1; i < rows_count; i++)
					{
						line = br.readLine();
						if (line == null) throw new Exception("File not well formatted.");
						string_tokens = line.split(fileSplitByTab);
						if (string_tokens.length != rows_count+1) throw new Exception("File not well formatted.");
						for (int j=2; j < rows_count+1; j++)
						{
							if( (j-1)>=i && !(matrix[i][j-1].getOccurrence().equalsIgnoreCase("X")))
							{
								double value = Double.valueOf(string_tokens[j]);
								matrix[i][j-1] = new DateOccurrence("", string_tokens[j]);
							} else matrix[i][j-1] = new DateOccurrence("", "0"); //delete else for have "X"
						}
					}
					if (subject_object_matrix.get(subject) != null)
					{
						if (subject_object_matrix.get(subject).get(object) == null)
							subject_object_matrix.get(subject).put(object, matrix);
						else throw new Exception("File not well formatted. An object is repeated.");
					} else 
					{
						HashMap<String, DateOccurrence[][]> obj_matrix = new HashMap<String, DateOccurrence[][]>();
						obj_matrix.put(object, matrix);
						subject_object_matrix.put(subject, obj_matrix);
					}
					line = br.readLine();
				} else throw new Exception("File not well formatted. " + string_tokens.length);		
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return subject_object_matrix;
	}
	

	public List<Fact> creatListOfFacts(List<String> temporaldefacto){
		
		Fact f = new Fact();
		List<Fact> allFacts = new ArrayList<Fact>();
		
		HashSet<ArrayList<String>> file=readCommaSeparatedFile(temporaldefacto);
		Iterator<ArrayList<String>> it = file.iterator();
		
		while (it.hasNext()){
			f=FactReader.readFact((ArrayList<String>) it.next());
			allFacts.add(f);
		}
		
		return allFacts;
	}
	
	
	
	public List<Fact> csvGS (File rsListFile){

		List<Fact> allFacts = new ArrayList<Fact>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
 
		try {
 
		br = new BufferedReader(new FileReader(rsListFile));
		while ((line = br.readLine()) != null) {
			if(line.contains("[")){
			line=line.substring(line.indexOf('[')+1,line.indexOf(']'));
			}
		        // use comma as separator
			String[] fact = line.split(cvsSplitBy);
			Fact f = new Fact();
			if (fact.length>0){f.add(Fact.Entry.SUBJECT, fact[0]);}
			if (fact.length>1){f.add(Fact.Entry.PREDICATE, fact[1]);}
			if (fact.length>2){f.add(Fact.Entry.OBJECT, fact[2]);}
			if (fact.length>3){f.add(Fact.Entry.YAGOSTART, fact[3]);}
			if (fact.length>4){f.add(Fact.Entry.YAGOEND, fact[4]);}
			if (fact.length>5){f.add(Fact.Entry.DATE, fact[5]);}
			if (fact.length>6){f.add(Fact.Entry.SCORE, fact[6]);}
			
			allFacts.add(f);
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	return allFacts;
  }
	
	public HashSet<ArrayList<String>> readTabSeparatedFile(List<String> file) {
		HashSet<ArrayList<String>> fileArray = new HashSet<ArrayList<String>>(); 
		
		
		for(String record:file){
			ArrayList<String> list = new ArrayList<String>();
			String subj=record.substring(0,record.indexOf("	"));
			list.add(subj);
			String remainingRecord= record.substring(record.indexOf("	")+1);
			//remainingRecord=remainingRecord.substring(remainingRecord.indexOf('[')+1,remainingRecord.indexOf(']'));
			
			int begin;
			do{
				
				begin= remainingRecord.indexOf("	");
				
				if(begin<0){
					String str = remainingRecord.substring(0,remainingRecord.length());
					str=str.trim();
					list.add(str);
				}
				else{
					String str = remainingRecord.substring(0, begin);
					str=str.trim();
					list.add(str);

					remainingRecord=remainingRecord.substring(begin+1);
				}
				
			}while (begin>0);
			
			fileArray.add(list);
		}
		return fileArray;

	}
	public List<Fact> readTabSeparatedFileLS(File rsListFile) {
		List<Fact> allFacts = new ArrayList<Fact>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "	";
 
		try {
 
		br = new BufferedReader(new FileReader(rsListFile));
		while ((line = br.readLine()) != null) {
			if(line.contains("[")){
			line=line.substring(line.indexOf('[')+1,line.indexOf(']'));
			}
		        // use comma as separator
			String[] fact = line.split(cvsSplitBy);
			Fact f = new Fact();
			if (fact.length>0){f.add(Fact.Entry.SUBJECT, fact[0]);}
			if (fact.length>1){f.add(Fact.Entry.PREDICATE, fact[1]);}
			if (fact.length>2){f.add(Fact.Entry.OBJECT, fact[2]);}
			if (fact.length>3){f.add(Fact.Entry.YAGOSTART, fact[3]);}
			if (fact.length>4){f.add(Fact.Entry.YAGOEND, fact[4]);}
			if (fact.length>5){f.add(Fact.Entry.DATE, fact[5]);}
			if (fact.length>6){f.add(Fact.Entry.SCORE, fact[6]);}
			
			allFacts.add(f);
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	return allFacts;
	}
	
	

	public HashSet<List<String>> readTSV(File rsListFile) throws IOException{
			HashSet<List<String>> hs = new HashSet<List<String>>();
	        StringTokenizer st ;
	        BufferedReader TSVFile = new BufferedReader(new FileReader(rsListFile));
	        
	        String dataRow = TSVFile.readLine(); // Read first line.

	        while (dataRow != null){
	            st = new StringTokenizer(dataRow,"\t");
	            List<String>dataArray = new ArrayList<String>() ;
	            while(st.hasMoreElements()){
	                dataArray.add(st.nextElement().toString());
	            }
	           
	            hs.add(dataArray);
	            dataRow = TSVFile.readLine(); // Read next line of data.
	        }
	        // Close the file once all data has been read.
	        TSVFile.close();
	        return hs;
	    }
	
	
	public HashSet<ArrayList<String>> readCSV(List<String> file) {
		HashSet<ArrayList<String>> fileArray = new HashSet<ArrayList<String>>(); 
		
		
		for(String record:file){
			ArrayList<String> list = new ArrayList<String>();
			String subj=record.substring(0,record.indexOf(","));
			list.add(subj);
			String remainingRecord= record.substring(record.indexOf(",")+1);
			//remainingRecord=remainingRecord.substring(remainingRecord.indexOf('[')+1,remainingRecord.indexOf(']'));
			
			int begin;
			do{
				
				begin= remainingRecord.indexOf(",");
				
				if(begin<0){
					
					list.add(remainingRecord.substring(0,remainingRecord.length()));
				}
				else{
					list.add(remainingRecord.substring(0, begin));

					remainingRecord=remainingRecord.substring(begin+1);
				}
				
			}while (begin>0);
			
			fileArray.add(list);
		}
		return fileArray;

	}
	
	
	
}
