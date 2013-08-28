package it.unimib.disco.FactExtractor;

import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.TemporalIntervalCreator.MatrixCreator;

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
	
	public static void main(String[] args) {
		
		// Resource URI extraction
		List<String> resURIs = ReadFiles.getURIs(new File(args[0]));
		logger.info("DBpedia resources list file parsed");
		
		ResourceFetcher rf = new ResourceFetcher();
		HashMap<String,OntModel> resourceModel =  rf.fetch(resURIs);
		logger.info("Retrieved all dbpedia's entity description");
		
		MatrixCreator dfe = new MatrixCreator();
		HashSet<Fact> l =  dfe.fetchtemporalfacts(resourceModel);
		logger.info("Retrieved all time points");

		try {
			File directory = new File (".");
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"repositoryDates.csv")));
			
			@SuppressWarnings("rawtypes")
			Iterator it = l.iterator();
			
			while (it.hasNext()){
				Fact f = new Fact();
				f = (Fact) it.next();
				bw.write(f.get(Entry.SUBJECT)+","+f.get(Entry.PREDICATE)+","+f.get(Entry.OBJECT)+","+"\n");
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
