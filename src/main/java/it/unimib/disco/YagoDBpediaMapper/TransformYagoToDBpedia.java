package it.unimib.disco.YagoDBpediaMapper;

import it.unimib.disco.FactExtractor.FactExtractor;
import it.unimib.disco.FactExtractor.ResourceFetcherDB;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;

public class TransformYagoToDBpedia {

private static Logger logger = Logger.getLogger(TransformYagoToDBpedia.class);
private static String prefix = "http://dbpedia.org/resource/";
	
public static void main (String []args){
	if (args.length < 1) {
		System.out.println("Use: java DBpediaCurrencyEvaluator <YagoResourceList_in.csv>");
	} else {
		// Resource URI extraction

		List<String> resURIs = ReadFiles.getURIs(new File(args[0]));
		logger.info("DBpedia resources list file parsed");
		
		ArrayList<String> dbpResourceArray=new ArrayList<String>();
		
		for (String resURI: resURIs){
			String resURIdbp=prefix+resURI;
			dbpResourceArray.add(resURIdbp);
		}
		
		ResourceFetcherDB rf = new ResourceFetcherDB();
		
		HashMap<String,OntModel> resourceModel =  rf.fetch(dbpResourceArray);
		logger.info("Retrieved all dbpedia's entity description");
		
		// date and fact extractor
		FactExtractor dfe = new FactExtractor();
					
		/*HashMap<String, ArrayList<String>> repositoryStatements =  dfe.fetchFacts(resourceModel);
		logger.info("Retrieved all dbpedia's entity description");
		*/	
		HashMap<String, HashMap<ArrayList<String>, String>> repositoryDates =  dfe.fetchDates(resourceModel);
		logger.info("Retrieved all dbpedia's property value of type date");
	
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("YagoResourceList_out.csv")));
			

			for(String res: repositoryDates.keySet()){
				HashMap<ArrayList<String>, String> predObj=repositoryDates.get(res);
				for(ArrayList<String> predObjKey : predObj.keySet() ){
					String obj = predObj.get(predObjKey);
					bw.write(predObjKey.get(0)+" "+obj+"\n");
				}
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
