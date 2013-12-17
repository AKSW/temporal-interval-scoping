package it.unimib.disco.TemporalFactExtraction;

import it.unimib.disco.FactExtractor.ResourceFetcherDB;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.aksw.distributions.Fact;
import org.aksw.distributions.Fact.Entry;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;

public class TemporalFactExtractor {

	/**
	 * @author rula
	 */
	private static Logger logger = Logger.getLogger(TemporalFactExtractor.class);
	
	public HashSet<Fact> extraction(List<String> resURIs){

		HashMap<String,OntModel> resourceModel =  new ResourceFetcherDB().fetch(resURIs);
		logger.info("Retrieved all dbpedia's entity description");
		

		HashSet<Fact> l =  new ResourceFetcherDB().fetchtemporalfacts(resourceModel);
		logger.info("Retrieved all time points");
		return l;
	}
	
	public static void main(String[] args) {
		
		// Resource URI extraction
		List<String> resURIs = ReadFiles.getURIs(new File(args[0]));
		logger.info("DBpedia resources list file parsed");
		
		HashSet<Fact> l = new YearExtractorDBp_predicates().extraction(resURIs);

		try {
			
			HashMap<String, HashSet<String>> res = new HashMap<String, HashSet<String>>();
			HashSet<String> obj = new HashSet<String>();
			
			@SuppressWarnings("rawtypes")
			Iterator it = l.iterator();
			
			while (it.hasNext()){
				Fact f = new Fact();
				f = (Fact) it.next();
				
				if (!res.containsKey(f.get(Entry.SUBJECT))){
					obj = new HashSet<String>();
					res.put(f.get(Entry.SUBJECT), obj);
				}
				obj=res.get(f.get(Entry.SUBJECT));
				obj.add(f.get(Entry.OBJECT));
				
				
			}
			File directory = new File (".");
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"repositoryDates.csv")));
			for (String uri: res.keySet()){
				bw.write(uri);
				
				@SuppressWarnings("rawtypes")
				Iterator i = res.get(uri).iterator();
				while (i.hasNext()){
					bw.write(","+i.next());
				}
				bw.write("\n");
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
