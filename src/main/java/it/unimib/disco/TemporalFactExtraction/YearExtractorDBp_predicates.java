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

public class YearExtractorDBp_predicates implements TemporalExtractionInterface {

	/**
	 * @author rula
	 */
	private static Logger logger = Logger.getLogger(YearExtractorDBp_predicates.class);
	
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
			File directory = new File (".");
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/repositoryDates/"+"repositoryDates_politician_predicate_dbp_reduced.csv")));
			
			@SuppressWarnings("rawtypes")
			Iterator it = l.iterator();
			
			while (it.hasNext()){
				Fact f = new Fact();
				f = (Fact) it.next();
				bw.write(f.get(Entry.SUBJECT)+"	"+f.get(Entry.PREDICATE)+"	"+f.get(Entry.OBJECT)+"\n");
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
