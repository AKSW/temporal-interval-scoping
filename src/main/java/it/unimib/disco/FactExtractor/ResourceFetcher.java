package it.unimib.disco.FactExtractor;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.n3.turtle.TurtleParseException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.shared.NotFoundException;
import com.hp.hpl.jena.util.FileManager;

/**
 * @author izzy
 * This component retrieves the DBPedia resources
 */
public class ResourceFetcher {
    
	private Logger logger = Logger.getLogger(ResourceFetcher.class);
	
	private static HashMap<String,OntModel> cache = new HashMap<String,OntModel>();
	
	public HashMap<String,OntModel> fetch(List<String> resURIs) {
		
		HashMap<String,OntModel> r = new HashMap<String,OntModel>();
		
		for (String resUri:resURIs){
			if(cache.keySet().contains(resUri)){
				r.put(resUri,cache.get(resUri));
			} else {
				OntModel m = fetch(resUri);
				cache.put(resUri, m);
				r.put(resUri,m);
			}
			
		}
		
		return r;
	}
	
	public OntModel fetch(String resUri){
		
		try {
			OntModel m = ModelFactory.createOntologyModel();
			logger.info("Creating ontology model for resource "+resUri);
			String n3Uri = resUri.replaceAll("resource", "data")+".n3";
			Model myRawModel = FileManager.get().loadModel(n3Uri, "N3");
			m.add(myRawModel);
			return m;
		} catch (TurtleParseException e) {
			OntModel m = ModelFactory.createOntologyModel();
			return m;
		} catch (NotFoundException nfe){
			OntModel m = ModelFactory.createOntologyModel();
			return m;
		}
		catch (JenaException je){
			OntModel m = ModelFactory.createOntologyModel();
			return m;
		}
		
	}

}
