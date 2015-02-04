package it.unimib.disco.ReadFiles;

import it.unimib.disco.Utilities.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;

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
	
	public Configuration readConfig(File rsListFile) {
		
		Configuration config = new Configuration();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "	";
 
		try {
 
		br = new BufferedReader(new FileReader(rsListFile));
		while ((line = br.readLine()) != null) {
		
		    // use comma as separator
			String[] linesep = line.split(cvsSplitBy);

			config.add(Configuration.Entry.Selection, Integer.parseInt(linesep[0]));
			config.add(Configuration.Entry.Normalization, Integer.parseInt(linesep[1]));
			config.add(Configuration.Entry.TopK, Integer.parseInt(linesep[2]));
			config.add(Configuration.Entry.Proxy, Integer.parseInt(linesep[3]));
			
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
 
	return config;
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
