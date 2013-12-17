package it.unimib.disco.YagoDBpediaMapper;

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

public class YagoFactMappedToDBpedia {

	private static Logger logger = Logger.getLogger(YagoPredicatesMappedToDBpedia.class);
		
		private static String prefix = "http://dbpedia.org/resource/";
		
		private static ArrayList<String> yagoSubject=new ArrayList<String>();
		private static HashMap<String, HashSet<String>> repositoryPredicates = new HashMap<String, HashSet<String>>();
		private static HashSet<ArrayList<String>> factYago = new HashSet<ArrayList<String>>();
		//private static HashSet<ArrayList<String>> factYagoReduced = new HashSet<ArrayList<String>>();
		private static HashSet<String> allFacts = new HashSet<String>();
		
		public static void main (String args[]){
			if (args.length < 2) {
				System.out.println("Use: java YagoFactMappedToDBpedia <yagoScopedFact file> <yagoPredicatesMappedDBpedia file>");
			} else {
				List<String> yagoFacts = ReadFiles.getURIs(new File(args[0]));
				logger.info("List of facts parsed");
				
				List<String> mappedProperties = ReadFiles.getURIs(new File(args[1]));
				logger.info("List of predicate parsed");
				
				//List<String> dbpURI = ReadFiles.getURIs(new File(args[2]));
				//logger.info("List of uris parsed");
				
				//list of the mapping between yago's properties and dbpedia's properties
				for(String yagoProperty: mappedProperties){
					HashSet<String> yagoDBpediaValue = new HashSet<String>();
					
					String yagoPropertyVal=yagoProperty.substring(0,yagoProperty.indexOf(';'));
					String yagoDBpediaProp= yagoProperty.substring(yagoProperty.indexOf(';')+1);
					yagoDBpediaProp=yagoDBpediaProp.substring(yagoDBpediaProp.indexOf('[')+1,yagoDBpediaProp.indexOf(']'));

					int begin;
					do{
						
						begin= yagoDBpediaProp.indexOf(',');
						
						if(begin<0){
							
							yagoDBpediaValue.add(yagoDBpediaProp.substring(0,yagoDBpediaProp.length()));
						}
						else{
							yagoDBpediaValue.add(yagoDBpediaProp.substring(0, begin));

							yagoDBpediaProp=yagoDBpediaProp.substring(begin+1);

						}
						
					}while (begin>0);
					
					repositoryPredicates.put(yagoPropertyVal, yagoDBpediaValue);
				}
				
				
				String str1,str2,str3,str4,str5,strSup1,strSup2,strSup3;
				ArrayList<String> dbpResourceArray=new ArrayList<String>();
				
				for(String str:yagoFacts){
					ArrayList<String> yagoFactarray=new ArrayList<String>();
					
					if (!str.contains("<")){
					}else{
					
					str1=prefix+str.substring(str.indexOf('<')+1,str.indexOf('>'));
					dbpResourceArray.add(str1);
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

					str5=strSup3.substring(strSup3.lastIndexOf(',')+1);
					yagoFactarray.add(str5);

					yagoSubject.add(str1);
					factYago.add(yagoFactarray);
				    
					}
					
				}
				
				logger.info("Parsed each line separately");

				/*for (ArrayList<String> fact: factYago){
					for (String uri: dbpURI)
						if (fact.get(0).equals(uri)){
							factYagoReduced.add(fact);
						}
				}*/
				
				// date and fact extractor
				YagoDBpediaMapper dfe = new YagoDBpediaMapper();
				
				
				allFacts=dfe.fetchDBpediaFacts(dfe.unifyDBpediaFacts(factYago,factYago), repositoryPredicates);
				for (String str: allFacts){
					System.out.println(str);
				}
				logger.info("Retrieved all dbpedia's property value of type date");
				
				
				File directory = new File (".");
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"gold_standard_people_complete.csv")));
					
					// header
					//bw.write("YagoFacts"+"\n" );
					for (String str: allFacts){

						bw.write(str+"\n");

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
