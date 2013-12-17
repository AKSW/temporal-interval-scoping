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
import javax.ws.rs.core.Response;

import org.aksw.distributions.Fact;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class SimpleDefactoClientV2 {

        public static final WebResource webResource = Client.create().resource("http://139.18.2.164:1234/getdefactotimes");
        public static CSVWriter writer;
       
        public static void main(String[] args) throws IOException, JSONException {
        	ReadFiles rf = new ReadFiles();
        	List<Fact> facts = rf.csvGS(new File(args[0]));
        	ArrayList<JSONObject> arr=new ArrayList<JSONObject>();
        	for(Fact fact:facts){
        		BufferedReader br_en = new BufferedReader(new FileReader(new File(args[1])));
            	//BufferedReader br_de = new BufferedReader(new FileReader(new File(args[2])));
            	//BufferedReader br_fr = new BufferedReader(new FileReader(new File(args[3])));
            	
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
            	    
            	    if (lineParts[0].equalsIgnoreCase(fact.get(Fact.Entry.SUBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
            	        
            	    		if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    	}	
            	    altSubjectLabels.put("en", surfaceForms);

            	    }
            	    else if(lineParts[0].equalsIgnoreCase(fact.get(Fact.Entry.OBJECT))){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
                	        
                	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
                	      
                	    }
            	    	altObjectLabels.put("en", surfaceForms);
            	    }
            	}
        		
        		/*found=0;
        		
        		while ((line = br_de.readLine()) != null&& found<2) {
            		String[] lineParts = line.split("\t");
            	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
            	    Set<String> surfaceForms = new HashSet<String>();
            	    String sub_de = fact.get(Fact.Entry.SUBJECT);
            	    sub_de = sub_de.substring(0, sub_de.indexOf("dbpedia"))+ "de." +  sub_de.substring(sub_de.indexOf("dbpedia"));
            	    if(subjectLabels.get("de")!=null){
            	    String labelS_de=subjectLabels.get("de").replace(" ", "_");
            	    sub_de=sub_de.substring(0,sub_de.lastIndexOf("/")+1)+ labelS_de;}
            	    
            	    String obj_de = fact.get(Fact.Entry.OBJECT);
            	    obj_de = obj_de.substring(0, obj_de.indexOf("dbpedia"))+ "de." +  obj_de.substring(obj_de.indexOf("dbpedia"));
            	    if(objectLabels.get("de")!=null){
            	    String labelO_de=objectLabels.get("de").replace(" ", "_");
            	    sub_de=sub_de.substring(0,sub_de.lastIndexOf("/")+1)+ labelO_de;}
            	    
            	    if (lineParts[0].equalsIgnoreCase(sub_de)){
            	    	found++;
            	    for ( String surfaceForm : surfaceFormsPart) {
            	        
            	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    }
            	    altSubjectLabels.put("de", surfaceForms);

            	    }
            	    
            	    else if(lineParts[0].equalsIgnoreCase(obj_de)){
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
            	    String sub_fr = fact.get(Fact.Entry.SUBJECT);
            	    sub_fr = sub_fr.substring(0, sub_fr.indexOf("dbpedia"))+ "fr." +  sub_fr.substring(sub_fr.indexOf("dbpedia"));
            	    if(subjectLabels.get("fr")!=null){
            	    String labelS_fr=subjectLabels.get("fr").replace(" ", "_");
            	    sub_fr=sub_fr.substring(0,sub_fr.lastIndexOf("/")+1)+ labelS_fr;}
            	    
            	    String obj_fr = fact.get(Fact.Entry.OBJECT);
            	    obj_fr = obj_fr.substring(0, obj_fr.indexOf("dbpedia"))+ "fr." +  obj_fr.substring(obj_fr.indexOf("dbpedia"));
            	    if(objectLabels.get("fr")!=null){
            	    String labelO_fr=objectLabels.get("fr").replace(" ", "_");
            	    obj_fr=obj_fr.substring(0,obj_fr.lastIndexOf("/")+1)+ labelO_fr;}
            	    
            	    if (lineParts[0].equalsIgnoreCase(sub_fr)){
            	    	found++;
            	    for ( String surfaceForm : surfaceFormsPart) {
            	        
            	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
            	    }
            	    altSubjectLabels.put("fr", surfaceForms);

            	    }
            	    else if(lineParts[0].equalsIgnoreCase(obj_fr)){
            	    	found++;
            	    	for ( String surfaceForm : surfaceFormsPart) {
                	        
                	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
                	      
                	    }
            	    	altObjectLabels.put("fr", surfaceForms);
            	    }
            	}*/
        		
        		
            String yagoStart = new MatrixCreator().stemDate(fact.get(Fact.Entry.YAGOSTART));
            String yagoEnd = new MatrixCreator().stemDate(fact.get(Fact.Entry.YAGOEND));
            
            br_en.close();
        	//br_de.close();
        	//br_fr.close();
            if(!subjectLabels.isEmpty()&&!objectLabels.isEmpty()&&!altSubjectLabels.isEmpty()&&!altObjectLabels.isEmpty()){
                System.out.println(fact.get(Fact.Entry.SUBJECT)+" "+ fact.get(Fact.Entry.PREDICATE)+" "+  fact.get(Fact.Entry.OBJECT)+" "+ 
                       Arrays.asList("en")+" "+ yagoStart +" "+  yagoEnd+" "+  "tiny"+" "+  subjectLabels+" "+  objectLabels+" "+  altSubjectLabels+" "+  altObjectLabels);
            
            // start the service
            JSONObject result = queryDefacto(fact.get(Fact.Entry.SUBJECT), fact.get(Fact.Entry.PREDICATE), fact.get(Fact.Entry.OBJECT),
                                Arrays.asList("en"), yagoStart, yagoEnd, "tiny", subjectLabels, objectLabels, altSubjectLabels, altObjectLabels);
            System.out.println(result);
               
              // JSONObject result = new JSONObject("{\"to\":\"0\",\"tiny\":{},\"oLabel\":\"Member of Parliament@en, Abgeordneter@de\",\"subject\":\"http://dbpedia.org/resource/Aaron_Mike_Oquaye\",\"sLabel\":\"Aaron Mike Oquaye@en, Mike Oquaye@de\",\"predicate\":\"leadername\",\"object\":\"http://dbpedia.org/resource/Member_of_Parliament\",\"from\":\"2005\",\"small\":{\"2005\":1},\"medium\":{\"2013\":1,\"2005\":4},\"large\":{\"2013\":1,\"2005\":4}}");
                
                arr.add(result);
            }
                 
        	}
        	
        	writeData(arr);
        }
        
     private static void writeData(ArrayList<JSONObject> arr) throws IOException, JSONException {
                
                for ( String contextLength : Arrays.asList("tiny", "small", "medium", "large") ) {
                	File directory = new File (".");
                	writer = new CSVWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/tmp"+contextLength+".csv")), '\t');
                       
                	for(JSONObject result:arr){
                	JSONObject years = result.getJSONObject(contextLength);
                	 if (years.length()>0){
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
                	 }
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
                ClientResponse clientResponse = webResource.queryParams(queryParams).post(ClientResponse.class);
                String message = clientResponse.getEntity(String.class);
                if ( clientResponse.getClientResponseStatus().getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode() ) 
                    throw new RuntimeException("\nWrong parameters given: \n" + message);
                else 
                    return new JSONObject(message);
                //return new JSONObject(webResource.queryParams(queryParams).post(String.class));
        }
}