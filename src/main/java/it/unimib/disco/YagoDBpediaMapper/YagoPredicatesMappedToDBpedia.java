package it.unimib.disco.YagoDBpediaMapper;

import it.unimib.disco.FactExtractor.ResourceFetcher;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;

public class YagoPredicatesMappedToDBpedia {

	private static Logger logger = Logger.getLogger(YagoPredicatesMappedToDBpedia.class);
	
	private static String prefix = "http://dbpedia.org/resource/";
	
	private static HashMap<String,ArrayList<String>> yagoFactsDBp=new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> yagoSubject=new ArrayList<String>();
	private static HashMap<String, HashSet<String>> repositoryPredicates = new HashMap<String, HashSet<String>>();
	
	public static void main (String args[]){
		if (args.length < 1) {
			System.out.println("Use: java YagoGoldStandardCreator <Yago fact list file>");
		} else {
			List<String> yagoFacts = ReadFiles.getURIs(new File(args[0]));
			logger.info("List of facts parsed");
			
			String str1,str2,str3,str4,str5,strSup1,strSup2,strSup3;
			
			for(String str:yagoFacts){
				ArrayList<String> yagoFactarray=new ArrayList<String>();
				
				if (!str.contains("<")){
				}else{
				str1=prefix+str.substring(str.indexOf('<')+1,str.indexOf('>'));
				yagoFactarray.add(str1);
					
				strSup1=str.substring(str.indexOf('>')+1);
				str2=strSup1.substring(strSup1.indexOf('<')+1,strSup1.indexOf('>'));
				yagoFactarray.add(str2);
					
				
				strSup2= strSup1.substring(strSup1.indexOf('>')+1);
				str3=prefix+strSup2.substring(strSup2.indexOf('<')+1,strSup2.indexOf('>'));

				yagoFactarray.add(str3);
				
				
				strSup3= strSup2.substring(strSup2.indexOf('>')+1);
				str4=strSup3.substring(strSup3.indexOf('<')+1,strSup3.indexOf('>'));
				yagoFactarray.add(str4);

				str5=strSup3.substring(strSup3.indexOf(',')+1);
				yagoFactarray.add(str5);
				
				if(!yagoFactsDBp.containsKey(str2)){
					yagoSubject.add(str1);
					yagoFactsDBp.put(str2, yagoFactarray);
			    }
				}
			}
			logger.info("Parsed each line separately");
			
			
			ResourceFetcher rf = new ResourceFetcher();
			
			HashMap<String,OntModel> resourceModel =  rf.fetch(yagoSubject);
			logger.info("Retrieved all dbpedia's entity description");
			
			// date and fact extractor
			YagoDBpediaMapper dfe = new YagoDBpediaMapper();
			
			for (String pred: yagoFactsDBp.keySet()){
				String subj=yagoFactsDBp.get(pred).get(0);
				
				repositoryPredicates =  dfe.fetchPredicates(resourceModel.get(subj), yagoFactsDBp.get(pred));
				logger.info("Retrieved all dbpedia's property value of type date");
			}
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("YagoPredicatesMappedToDBpedia_out.csv")));
				
				// header

				bw.write("YagoPredicate"+";"+"DBpediaPredicate"+"\n" );
				for (String str: repositoryPredicates.keySet()){
					bw.write(str+";"+ repositoryPredicates.get(str)+"\n");
				}
						
				
				bw.flush();
				bw.close();
				logger.info("Results file built");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
