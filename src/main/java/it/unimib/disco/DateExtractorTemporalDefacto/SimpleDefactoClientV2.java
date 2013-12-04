package it.unimib.disco.DateExtractorTemporalDefacto;
import it.unimib.disco.FactExtractor.ResourceFetcherDB;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.aksw.distributions.Fact;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class SimpleDefactoClientV2 {

        public static final WebResource webResource = Client.create().resource("http://139.18.2.164:1234/getdefactotimes");
        
        public static void main(String[] args) throws IOException, JSONException {
        	ReadFiles rf = new ReadFiles();
        	List<Fact> facts = rf.csvGS(new File(args[0]));
        	
        	
        	
        	for(Fact fact:facts){
        		BufferedReader br_en = new BufferedReader(new FileReader(new File(args[1])));
            	BufferedReader br_de = new BufferedReader(new FileReader(new File(args[2])));
            	BufferedReader br_fr = new BufferedReader(new FileReader(new File(args[3])));
            	
            	String line;
        		int found = 0;
        		OntModel subjectModel =  new ResourceFetcherDB().fetch(fact.get(Fact.Entry.SUBJECT));
        		Map<String,String> subjectLabels =  new ResourceFetcherDB().fetchLabels(subjectModel);
        		
        		OntModel objectModel =  new ResourceFetcherDB().fetch(fact.get(Fact.Entry.OBJECT));
        		Map<String,String> objectLabels =  new ResourceFetcherDB().fetchLabels(objectModel);
        		
        		Map<String, Set<String>> altSubjectLabels = new HashMap<>();
        		Map<String, Set<String>> altObjectLabels = new HashMap<>();
        		
        		while ((line = br_en.readLine()) != null && found<2) {
            		String[] lineParts = line.split("\t");
            	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
            	    Set<String> surfaceForms = new HashSet<String>();
            	    
            	    if (lineParts[0].contains(fact.get(Fact.Entry.SUBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
            	        
            	    		if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    	}	
            	    altSubjectLabels.put("en", surfaceForms);

            	    }
            	    else if(lineParts[0].contains(fact.get(Fact.Entry.OBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
                	        
                	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
                	      
                	    }
            	    	altObjectLabels.put("en", surfaceForms);
            	    }
            	}
        		
        		found=0;
        		
        		while ((line = br_de.readLine()) != null&& found<2) {
            		String[] lineParts = line.split("\t");
            	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
            	    Set<String> surfaceForms = new HashSet<String>();
            	    
            	    if (lineParts[0].contains(fact.get(Fact.Entry.SUBJECT))){
            	    	found++;
            	    for ( String surfaceForm : surfaceFormsPart) {
            	        
            	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    }
            	    altSubjectLabels.put("de", surfaceForms);

            	    }
            	    else if(lineParts[0].contains(fact.get(Fact.Entry.OBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
                	        
                	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
                	        
                	    }
            	    	altObjectLabels.put("de", surfaceForms);
            	    }
            	}
        		
        		found=0;
        		
        		while ((line = br_fr.readLine()) != null&& found<2) {
            		String[] lineParts = line.split("\t");
            	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
            	    Set<String> surfaceForms = new HashSet<String>();
            	    
            	    if (lineParts[0].contains(fact.get(Fact.Entry.SUBJECT))){
            	    	found++;
            	    for ( String surfaceForm : surfaceFormsPart) {
            	        
            	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    }
            	    altSubjectLabels.put("fr", surfaceForms);

            	    }
            	    else if(lineParts[0].contains(fact.get(Fact.Entry.OBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
                	        
                	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
                	      
                	    }
            	    	altObjectLabels.put("fr", surfaceForms);
            	    }
            	}
        		
        		
            String yagoStart = new MatrixCreator().stemDate(fact.get(Fact.Entry.YAGOSTART));
            String yagoEnd = new MatrixCreator().stemDate(fact.get(Fact.Entry.YAGOEND));
            
            br_en.close();
        	br_de.close();
        	br_fr.close();
                System.out.println(fact.get(Fact.Entry.SUBJECT)+" "+ fact.get(Fact.Entry.PREDICATE)+" "+  fact.get(Fact.Entry.OBJECT)+" "+ 
                       Arrays.asList("en", "de", "fr")+" "+ yagoStart +" "+  yagoEnd+" "+  "tiny"+" "+  subjectLabels+" "+  objectLabels+" "+  altSubjectLabels+" "+  altObjectLabels);
             // start the service
            JSONObject result = queryDefacto(fact.get(Fact.Entry.SUBJECT), fact.get(Fact.Entry.PREDICATE), fact.get(Fact.Entry.OBJECT),
                                 Arrays.asList("en", "de", "fr"), yagoStart, yagoEnd, "tiny", subjectLabels, objectLabels, altSubjectLabels, altObjectLabels);

            writeData(result);
                
        	}
        	
        }
        
        private static void writeData(JSONObject result) throws IOException, JSONException {
                
                for ( String contextLength : Arrays.asList("tiny", "small", "medium", "large") ) {
                	File directory = new File (".");
                	CSVWriter writer = new CSVWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/tmp"+contextLength+".csv")), '\t');
                        
                	JSONObject years = result.getJSONObject(contextLength);
                        
                        for (int i = 0; i < years.names().length(); i++) {
                                
                                List<String> output = new ArrayList<>();
                                output.add(result.getString("subject"));
                                output.add(result.getString("predicate"));
                                output.add(result.getString("object"));
                                output.add(result.getString("from"));
                                output.add(result.getString("to"));
                                output.add(years.names().getString(i));
                                output.add("" + years.get(years.names().getString(i)));
                                writer.writeNext(output.toArray(new String[]{}));
                        }
                        writer.close();
                }
        }

        public static JSONObject queryDefacto(String subjectUri, String propertyUri, String objectUri, List<String> languages,
                                                                        String from, String to, String contextSize, 
                                                                        Map<String,String> subjectLabels, Map<String,String> objectLabels, 
                                                                        Map<String,Set<String>> altSubjectLabels, Map<String,Set<String>> altObjectLabels) 
                                                                                        throws UniformInterfaceException, ClientHandlerException, JSONException{
                
                MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

                // subject
                queryParams.add("s", subjectUri);
                for (String lang : languages) {
                		if(subjectLabels.get(lang)!=null){
                        queryParams.add("sLabel", subjectLabels.get(lang) + "@" + lang);}
                        if(altSubjectLabels.get(lang) !=null){
                        for (String altLabel : altSubjectLabels.get(lang)) {

                                // if (j++ == 50) break;
                                queryParams.add("sAltLabel", altLabel + "@" + lang);
                        }}
                }

                // predicate
                queryParams.add("p", propertyUri);

                // object
                queryParams.add("o", objectUri);
                for (String lang : languages) {
                		
                		if(objectLabels.get(lang)!=null){
                        queryParams.add("oLabel", objectLabels.get(lang) + "@" + lang);}
                        if(altObjectLabels.get(lang) !=null){
                        for (String altLabel : altObjectLabels.get(lang)) {

                                // if (j++ == 50) break;
                                queryParams.add("oAltLabel", altLabel + "@" + lang);
                        }
                        }
                }

                // languages
                for (String lang : languages)
                        queryParams.add("language", lang);
                
                // context size: tiny, small, medium, large
                queryParams.add("contextSize", "tiny");
                
                // time period
                        queryParams.add("from", from);
                        queryParams.add("to", to);

                // start the service
                return new JSONObject(webResource.queryParams(queryParams).post(String.class));
        }
}