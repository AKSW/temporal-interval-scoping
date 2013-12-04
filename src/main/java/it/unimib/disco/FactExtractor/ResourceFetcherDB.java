package it.unimib.disco.FactExtractor;

import it.unimib.disco.MatrixCreator.MatrixCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.aksw.distributions.Fact;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.n3.turtle.TurtleParseException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.shared.NotFoundException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author izzy
 * This component retrieves the DBPedia resources
 */
public class ResourceFetcherDB {
    
	private Logger logger = Logger.getLogger(ResourceFetcherDB.class);
	
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
	
	
	
public HashSet<Fact> fetchtemporalfacts (HashMap<String,OntModel> models){
		
		HashSet<Fact> result = new HashSet<Fact>();
		
		for(String rUri: models.keySet()){

			OntModel m = models.get(rUri);
			
			StmtIterator iter = m.listStatements();
				
			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) {
				
			    Statement stmt      = iter.next();  // get next statement
			    Fact f = new Fact();

			   // Resource  subject   = stmt.getSubject();     // we are considering only outgoing link so the subject is always rUri
			    Property  predicate = stmt.getPredicate();   // get the predicate
 
			    RDFNode   object    = stmt.getObject();      // get the object

			    if (object instanceof Resource) {

			    } else {
			    	
			        // object is a literal
			    	if(new MatrixCreator().verifyDate(object)){
			    		String dateStr= new MatrixCreator().stemDate(object.toString());
			    		
			    		if (new MatrixCreator().stringToLong(dateStr).before(new MatrixCreator().stringToLong("2013-12-31"))){
			    			f.add(Fact.Entry.SUBJECT, rUri);
			    	        f.add(Fact.Entry.PREDICATE, predicate.toString());
			    	        f.add(Fact.Entry.OBJECT, dateStr);
			    	        result.add(f);
			    		}
			    	}
			    }
			    
			}
			/********add year 2013*******/
			Fact f = new Fact();
			f.add(Fact.Entry.SUBJECT, rUri);
			f.add(Fact.Entry.PREDICATE, "http://dbpedia.org/property/years");
			f.add(Fact.Entry.OBJECT, "2013");
			result.add(f);

		
		}
		return result;
	}

public Map<String,String> fetchLabels (OntModel m){
	Map<String,String> labels = new HashMap<String,String>();
//	Map<String,Set<String>> altLabels = new HashMap<String,Set<String>>();
	
	NodeIterator rdfsIterator = m.listObjectsOfProperty(RDFS.label);
    while (rdfsIterator.hasNext()) {
            RDFNode rdfNode = (RDFNode) rdfsIterator.next();
            
            String language = rdfNode.asLiteral().getLanguage();
            String label = rdfNode.asLiteral().getLexicalForm();
            if(language.equalsIgnoreCase("en")||language.equalsIgnoreCase("de")||language.equalsIgnoreCase("fr")){
            labels.put(language,label);
            }
    }
   /* NodeIterator skosAltIterator = m.listObjectsOfProperty(ResourceFactory.createProperty("http://www.w3.org/2004/02/skos/core#altLabel"));
    while (skosAltIterator.hasNext()) {
            RDFNode rdfNode = (RDFNode) skosAltIterator.next();
            
            String language = rdfNode.asLiteral().getLanguage();
            String label = rdfNode.asLiteral().getLexicalForm();
            if(language.equalsIgnoreCase("en")||language.equalsIgnoreCase("de")||language.equalsIgnoreCase("fr")){
            altLabels.get(language).add(label);
            }
    }*/

	return labels;
	}
}
