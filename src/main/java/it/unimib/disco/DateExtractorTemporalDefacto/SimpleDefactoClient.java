package it.unimib.disco.DateExtractorTemporalDefacto;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class SimpleDefactoClient {

        public static final WebResource webResource = Client.create().resource("http://139.18.2.164:1234/getdefactotimes");
        
        public static void main(String[] args) throws IOException, JSONException {
        	ReadFiles rf = new ReadFiles();
        	List<Fact> facts = rf.csvGS(new File(args[0]));

			
        	BufferedReader br = new BufferedReader(new FileReader(new File(args[1])));
        	String line;
        	Map<String,Set<String>> urisToLabels = new HashMap<String,Set<String>>();
        	while ((line = br.readLine()) != null) {
        		String[] lineParts = line.split("\t");
        	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
        	    Set<String> surfaceForms = new HashSet<String>();
        	    
        	    for ( String surfaceForm : surfaceFormsPart) {
        	        
        	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
        	    }
        	    System.out.println(surfaceForms);
        	    // carefull since this elimates the dbpedia prefix
        	    urisToLabels.put(lineParts[0].replace("http://dbpedia.org/resource/", ""), surfaceForms);
        	}
        	br.close();
        	
        	List<String> surfaceFormsEn = ReadFiles.getURIs(new File(args[1]));
        	List<String> surfaceFormsDe = ReadFiles.getURIs(new File(args[2]));
        	List<String> surfaceFormsFr = ReadFiles.getURIs(new File(args[3]));
        	Map<String,Map<String,Set<String>>> langForms= new HashMap<String,Map<String,Set<String>>>();
        	langForms = getlangForms(getsurface(surfaceFormsEn),getsurface(surfaceFormsDe),getsurface(surfaceFormsFr));
        	
        	for(Fact fact:facts){

                Map<String, String> subjectLabels = new HashMap<>();
                subjectLabels.put("en", "Albert Einstein");
                subjectLabels.put("de", "Albert Einstein");
                subjectLabels.put("fr", "Albert Einstein");
                
                Set<String> enSlabels=langForms.get(fact.get(Fact.Entry.SUBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("en");
                Set<String> deSlabels=langForms.get(fact.get(Fact.Entry.SUBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("de");
                Set<String> frSlabels=langForms.get(fact.get(Fact.Entry.SUBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("fr");
               
                Map<String, String> objectLabels = new HashMap<>();
                objectLabels.put("en", "Nobel Prize in Physics");
                objectLabels.put("de", "Nobelpreis für Physik");
                objectLabels.put("fr", "Prix Nobel de physique");
                
                Map<String, Set<String>> altSubjectLabels = new HashMap<>();
                Set<String> enSAltLabels = new HashSet<>(Arrays.asList(enSlabels.toArray(new String[0]))); // add more for each language
                Set<String> deSAltLabels = new HashSet<>(Arrays.asList(deSlabels.toArray(new String[0]))); // from the surface form files
                Set<String> frSAltLabels = new HashSet<>(Arrays.asList(frSlabels.toArray(new String[0])));
                altSubjectLabels.put("en", enSAltLabels);
                altSubjectLabels.put("de", deSAltLabels);
                altSubjectLabels.put("fr", frSAltLabels);
                
                Set<String> enOlabels=langForms.get(fact.get(Fact.Entry.OBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("en");
                Set<String> deOlabels=langForms.get(fact.get(Fact.Entry.OBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("de");
                Set<String> frOlabels=langForms.get(fact.get(Fact.Entry.OBJECT).toString().replace("http://dbpedia.org/resource/", "")).get("fr");
                
                Map<String, Set<String>> altObjectLabels = new HashMap<>();
                Set<String> enOAltLabels = new HashSet<>(Arrays.asList(enOlabels.toArray(new String[0]))); // add more for each language
                Set<String> deOAltLabels = new HashSet<>(Arrays.asList(deOlabels.toArray(new String[0]))); // from the surface form files
                Set<String> frOAltLabels = new HashSet<>(Arrays.asList(frOlabels.toArray(new String[0])));
                altObjectLabels.put("en", enOAltLabels);
                altObjectLabels.put("de", deOAltLabels);
                altObjectLabels.put("fr", frOAltLabels);
                
                System.out.println(fact.get(Fact.Entry.SUBJECT)+" "+ fact.get(Fact.Entry.PREDICATE)+" "+  fact.get(Fact.Entry.OBJECT)+" "+ 
                        Arrays.asList("en", "de", "fr")+" "+  fact.get(Fact.Entry.YAGOSTART)+" "+  fact.get(Fact.Entry.YAGOEND)+" "+  "tiny"+" "+  subjectLabels+" "+  objectLabels+" "+  altSubjectLabels+" "+  altObjectLabels);

                // start the service
               /* JSONObject result = queryDefacto(fact.get(Fact.Entry.SUBJECT), fact.get(Fact.Entry.PREDICATE), fact.get(Fact.Entry.OBJECT),
                                Arrays.asList("en", "de", "fr"), fact.get(Fact.Entry.YAGOSTART), fact.get(Fact.Entry.YAGOEND), "tiny", subjectLabels, objectLabels, altSubjectLabels, altObjectLabels);

                writeData(result);*/
        	
        	}
        }
        
        

		private static void writeData(JSONObject result) throws IOException, JSONException {
                
                for ( String contextLength : Arrays.asList("tiny", "small", "medium", "large") ) {
                	File directory = new File (".");
                	BufferedWriter bw;
            		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/tmp"+contextLength+".csv")));
              
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
                                bw.write(output.get(0)+";"+output.get(1)+";"+output.get(2)+";"+output.get(3)+";"+output.get(4)+";"+output.get(5)+";"+output.get(6)+"\n");
                        }
                        bw.flush();
                        bw.close();
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

                        queryParams.add("sLabel", subjectLabels.get(lang) + "@" + lang);
                        for (String altLabel : altSubjectLabels.get(lang)) {

                                // if (j++ == 50) break;
                                queryParams.add("sAltLabel", altLabel + "@" + lang);
                        }
                }

                // predicate
                queryParams.add("p", propertyUri);

                // object
                queryParams.add("o", objectUri);
                for (String lang : languages) {

                        queryParams.add("oLabel", objectLabels.get(lang) + "@" + lang);
                        for (String altLabel : altObjectLabels.get(lang)) {

                                // if (j++ == 50) break;
                                queryParams.add("oAltLabel", altLabel + "@" + lang);
                        }
                }

                // languages
                for (String lang : languages)
                        queryParams.add("language", lang);
                
                // context size: tiny, small, medium, large
                queryParams.add("contextSize", contextSize);
                
                // time period
                        queryParams.add("from", from);
                        queryParams.add("to", to);

                // start the service
                return new JSONObject(webResource.queryParams(queryParams).post(String.class));
        }
        
        public  static Map<String,Set<String>> getsurface(List<String> surfaceFile)
        {
        	Map<String,Set<String>> urisToLabels = new HashMap<String,Set<String>>();
        	for ( String line : surfaceFile ) {
        	            
        	    String[] lineParts = line.split("\t");
        	    String[] surfaceFormsPart = Arrays.copyOfRange(lineParts, 1, lineParts.length);
        	    Set<String> surfaceForms = new HashSet<String>();
        	    
        	    for ( String surfaceForm : surfaceFormsPart) {
        	        
        	        if ( surfaceForm.length() >= 3 ) surfaceForms.add(surfaceForm);
        	    }
        	    
        	    // carefull since this elimates the dbpedia prefix
        	    urisToLabels.put(lineParts[0].replace("http://dbpedia.org/resource/", ""), surfaceForms);
        	    
        	}
        	return urisToLabels;
        }
        
        public static Map<String, Map<String, Set<String>>> getlangForms(
				Map<String, Set<String>> surfaceEn,
				Map<String, Set<String>> surfaceDe,
				Map<String, Set<String>> surfaceFr) {
        	
        	Map<String,Map<String,Set<String>>> urilangForms= new HashMap<String,Map<String,Set<String>>>();
        	
        	for(String subjEn:surfaceEn.keySet()){
        		Map<String,Set<String>> langForm = new HashMap<String,Set<String>>();
        		langForm.put("en", surfaceEn.get(subjEn));
        		langForm.put("de", surfaceDe.get(subjEn));
        		langForm.put("en", surfaceFr.get(subjEn));
        		
        		urilangForms.put(subjEn, langForm);
			}
			return urilangForms;
		}
        
}
