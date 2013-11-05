package it.unimib.disco.FreebasePreparation;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class MqlRead_statistics {
	  
	public static Properties properties = new Properties();
	  
	public static void main(String[] args) {
	    
		try {
	    	File directory = new File (".");
	    	properties.load(new FileInputStream(directory.getAbsolutePath()+"/src/main/resources/freebase.properties"));
	     
	      HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();


	      BufferedWriter bw;
		  bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/"+"gs_freebase_players.csv")));
	      List<String> ls_resources = ReadFiles.getURIs(new File(args[0]));
	      String subject = null;
	      for(int z=0;z<ls_resources.size();z++){
	      
	      String query = "[{\"type\":\"/soccer/football_player\",\"/type/object/id\":\""+ls_resources.get(z)+"\",\"statistics\":[{\"team\":null,\"appearances\":null,\"total_goals\":null,\"from\":null,\"to\":null}]}]";
	      GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
	      url.put("query", query);
	      url.put("key", properties.get("API_KEY"));
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();

	      JSONObject s = new JSONObject(httpResponse.parseAsString());

	      JSONArray array_result = s.getJSONArray("result");
	      
		  //Record r = new Record();
		  
	      if(array_result!=null){
	      for (int i = 0; i < array_result.length(); i++) {
	    	    JSONObject res = array_result.getJSONObject(i);
	    	    JSONArray array_statistics = res.getJSONArray("statistics");
	    	    
	    	    if(array_statistics!=null){
	    	    for(int j = 0; j < array_statistics.length(); j++){
	    	    	JSONObject rec = array_statistics.getJSONObject(j);
	    	    	subject=ls_resources.get(z);
	    	    	String team = rec.get("team").toString();
	    	    	String from = rec.get("from").toString();
	    	    	String to = rec.get("to").toString();
	    	
		    	   bw.write(subject+"	 "+"plays_for"+"	 "+team+"	 "+from+"	 "+to+"\n" );

	    	    }
	    	    }

	      }
	      }
	      }
		  bw.flush();
		  bw.close();
		  } catch (Exception ex) {
		     ex.printStackTrace();
		  }
		  
		}
	
}

