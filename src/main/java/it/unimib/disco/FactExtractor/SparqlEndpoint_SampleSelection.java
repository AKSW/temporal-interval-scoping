package it.unimib.disco.FactExtractor;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.semanticweb.yars.nx.Resource;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.resultset.ResultSetFormat;



public class SparqlEndpoint_SampleSelection {

	private String service;
	private String apikey;
	public static final ResultSetFormat rFmt = ResultSetFormat.syntaxRDF_XML;
	
	public final static Resource CONTEXT = new Resource("<http://dbpedia.org/resource/Ahmed_Barusso>");
	
	public SparqlEndpoint_SampleSelection(String service, String apikey) {
		this.service = service;
		this.apikey = apikey;
	}
	
	public ResultSet executeQuery(String queryString) throws Exception {
		 Query query = QueryFactory.create(queryString) ;
		 QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(this.service, query);
		 qexec.addParam("apikey", this.apikey);
		 ResultSet results = qexec.execSelect() ;
		 return results;

	}
	public static void main(String[] args) throws Exception{
		
		List<String> yagoFacts = getFacts(new File(args[0]));
		
		String sparqlService = "http://dbpedia.org/sparql";
		String apikey = "YOUR API KEY";

		File directory = new File (".");
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/"+"sample_persons_age1930.csv")));
		List<String> ls_resources = ReadFiles.getURIs(new File(args[0]));
		
		HashSet<String> result = new HashSet<String>();
		
		for(int z=0;z<ls_resources.size();z++)
	    {
			
		//Execute a query
		/*String queryString = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
						"PREFIX dbo: <http://dbpedia.org/ontology/>"+
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"SELECT ?s "+
				"WHERE"+
			  "{ ?s rdf:type dbo:SoccerPlayer ."+
			  "?s dbo:birthDate ?birth . " +
			  "FILTER (?birth > \"1983-01-01\"^^xsd:date) ."+
			  "}";*/
		String label = ls_resources.get(z).substring(ls_resources.get(z).lastIndexOf('/')+1).replace('_', ' ');
		if (label.contains("\"")){
			String escape = "\\";
			label.replace("\"", escape);
		}
		System.out.println(label);
		String queryString = 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
						"PREFIX dbo: <http://dbpedia.org/ontology/>"+
						"SELECT distinct ?s WHERE {"+
						"?s dbo:spouse ?o ."+
						"?s rdfs:label ?label."+
						"FILTER regex(?label, \""+label+"\", \"i\" )"+
						 "?s dbo:birthDate ?birth ."+
						"FILTER (?birth > \"1930-01-01\"^^xsd:date) ."+
						"}";
		
		SparqlEndpoint_SampleSelection test = new SparqlEndpoint_SampleSelection(sparqlService,apikey);
		ResultSet results = test.executeQuery(queryString);
		
		for ( ; results.hasNext() ; ) {
			QuerySolution soln = results.nextSolution() ;
		    RDFNode subject = soln.get("s") ;

		    result.add(subject.toString());
		}
	    }
		for(String dbpUri: yagoFacts ){ 
		
			for (String sparqlUri:result )
				if(dbpUri.contains(sparqlUri)){
					
					bw.write(dbpUri+"\n");
			}
			
		}
		
		bw.flush();
		bw.close();
	}
	
	protected static List<String> getFacts(File rsListFile) {
		
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
}