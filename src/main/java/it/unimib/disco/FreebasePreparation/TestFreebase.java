package it.unimib.disco.FreebasePreparation;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestFreebase {
  public static Properties properties = new Properties();
  public static void main(String[] args) throws IllegalStateException, IOException {

    File directory = new File (".");
      
      try {
    	  properties.load(new FileInputStream(directory.getAbsolutePath()+"/src/main/resources/freebase.properties"));

    	        /*HttpTransport httpTransport = new NetHttpTransport();
    	        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
    	        JSONParser parser = new JSONParser();
    	        GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/search");
    	        url.put("query", "Cee Lo Green");
    	        url.put("filter", "(all type:/music/artist created:\"The Lady Killer\")");
    	        url.put("limit", "10");
    	        url.put("indent", "true");
    	        url.put("key", properties.get("API_KEY"));
    	        HttpRequest request = requestFactory.buildGetRequest(url);
    	        HttpResponse httpResponse = request.execute();
    	        JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
    	        JSONArray results = (JSONArray)response.get("result");
    	        for (Object result : results) {
    	          System.out.println(JsonPath.read(result,"$.name").toString());
    	        }*/
    	  HttpTransport httpTransport = new NetHttpTransport();
          HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
          JSONParser parser = new JSONParser();
          String topicId = "/m/07r1h";
          GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/topic" + topicId);
          url.put("key", properties.get("API_KEY"));
          HttpRequest request = requestFactory.buildGetRequest(url);
          HttpResponse httpResponse = request.execute();
          JSONObject topic = (JSONObject)parser.parse(httpResponse.parseAsString());
         
          System.out.println(topic);
          System.out.println(JsonPath.read(topic,"$.property['/people/person/spouse_s'].values[0]").toString());
    	      } catch (Exception ex) {
    	        ex.printStackTrace();
    	      }
    	    }
    	
    	  
        /*  String serviceURL = "https://www.googleapis.com/freebase/v1/rdf";
          HttpClient httpclient = new DefaultHttpClient();
          String topicId = "/m/07r1h";
          String url = serviceURL + topicId + "?key=" + properties.get("API_KEY");
          HttpGet request = new HttpGet(url);
          HttpResponse httpResponse = httpclient.execute(request);
          Model model = ModelFactory.createDefaultModel();
          model.read(httpResponse.getEntity().getContent(), null, "TURTLE");
          Resource topic = model.getResource("http://rdf.freebase.com/ns/" + topicId.substring(1).replace('/','.'));
          Property labelProperty = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
          System.out.println(topic.getProperty(labelProperty).getString());
        } catch (Exception ex) {
          ex.printStackTrace();
        }*/
      
  }
  
  /*public static Model fetchFR(String topicId) throws ClientProtocolException, IOException{
	  String serviceURL = "https://www.googleapis.com/freebase/v1/rdf";
      HttpClient httpclient = new DefaultHttpClient();
	  String url = serviceURL + topicId + "?key=" + properties.get("API_KEY");
      HttpGet request = new HttpGet(url);
      HttpResponse httpResponse = httpclient.execute(request);
		
	  try {
	      Model model = ModelFactory.createDefaultModel();
	      model.read(httpResponse.getEntity().getContent(), null, "TURTLE");
	      Resource topic = model.getResource("http://rdf.freebase.com/ns/" + topicId.substring(1).replace('/','.'));
	      Property labelProperty = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
	      System.out.println(topic.getProperty(labelProperty).getString());
	      return model;
	      } catch (TurtleParseException e) {
	    	  Model model = ModelFactory.createDefaultModel();
	    	  return model;
			} catch (NotFoundException nfe){
				 Model model = ModelFactory.createDefaultModel();
				 return model;
			}
			catch (JenaException je){
				 Model model = ModelFactory.createDefaultModel();
				 return model;
				
			}
	  
	}
   */
