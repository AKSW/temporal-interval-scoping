package it.unimib.disco.FreebasePreparation;
import it.unimib.disco.MatrixCreator.MatrixCreator;
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

public class YearExtractorFrb_predicates {
	  
	public static Properties properties = new Properties();
	  
	public static void main(String[] args) {
	    
		try {
	    	File directory = new File (".");
	    	properties.load(new FileInputStream(directory.getAbsolutePath()+"/src/main/resources/freebase.properties"));
	     
	      HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();


	      BufferedWriter bw;
		  bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/repositoryDates/"+"repositoryDates_player_predicate_frb.csv")));
	      List<String> ls_resources_frb = ReadFiles.getURIs(new File(args[0]));
	      
	      List<String> ls_resources_dbp = ReadFiles.getURIs(new File(args[1]));
	      
	      String subject = null;
	      for(int z=0;z<ls_resources_frb.size();z++){
	    	  subject=ls_resources_frb.get(z);
	    	  
	      String query="[{  \"/type/object/id\":\""+ls_resources_frb.get(z)+"\",  \"/soccer/football_player/statistics\": [{    \"from\": null,    \"to\": null  }],  " +
	      		"\"/soccer/football_player/loans\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/soccer/football_player/transfers\": [{    \"date_transfer_was_agreed\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/people/person/date_of_birth\": null,  " +
	      		"\"/people/person/spouse_s\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/people/person/employment_history\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/people/person/education\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/people/person/places_lived\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/sports/pro_athlete/teams\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/sports/pro_athlete/sports_played_professionally\": [{    \"career_start\": null,    \"career_end\": null,    \"optional\": \"optional\"  }]}]";
	      //String query = "[{\"type\":\"/soccer/football_player\",\"/type/object/id\":\""+ls_resources.get(z)+"\",\"statistics\":[{\"team\":null,\"appearances\":null,\"total_goals\":null,\"from\":null,\"to\":null}]}]";
	      GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
	      url.put("query", query);
	      url.put("key", properties.get("API_KEY"));
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();
	      
	      
	      subject=ls_resources_frb.get(z).substring(ls_resources_frb.get(z).lastIndexOf('/')+1);
		  for(int j=0;j<ls_resources_dbp.size();j++){
			  if(ls_resources_dbp.get(j).toLowerCase().contains(subject)){
			  
				  subject=ls_resources_dbp.get(j);
			  }
		  }
	      JSONObject s = new JSONObject(httpResponse.parseAsString());

	      JSONArray array_result = s.getJSONArray("result");
	      
	      
	      if(array_result!=null){
	      for (int i = 0; i < array_result.length(); i++) {
	    	    JSONObject res = array_result.getJSONObject(i);

	    	    if(res.getJSONArray("/soccer/football_player/statistics")!=null){
	    	    for(int j = 0; j < res.getJSONArray("/soccer/football_player/statistics").length(); j++){
	    	    	JSONObject rec = res.getJSONArray("/soccer/football_player/statistics").getJSONObject(j);
	    	    	String from = rec.get("from").toString();
	    	    	
	    	    	String to = rec.get("to").toString();
	    	    	from = new MatrixCreator().stemDate(from.toString());
	    	    	to = new MatrixCreator().stemDate(to.toString());
	    	    	if (!from.contains("null")){
	    	    		bw.write(subject+","+"/soccer/football_player/statistics/from"+","+from+"\n" );
	    	    	}
	    	    	if(!to.contains("null")){
	    	    	bw.write(subject+","+"/soccer/football_player/statistics/to"+","+to+"\n" );
	    	    	
	    	    	}

	    	    }
	    	    }
	    	    if(res.getJSONArray("/soccer/football_player/loans")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/soccer/football_player/loans").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/soccer/football_player/loans").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/soccer/football_player/loans/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/soccer/football_player/loans/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/soccer/football_player/transfers")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/soccer/football_player/transfers").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/soccer/football_player/transfers").getJSONObject(j);
		    	    	String from = rec.get("date_transfer_was_agreed").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());

		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/soccer/football_player/transfers/date_transfer_was_agreed"+","+from+"\n" );
		    	    	}
		    	    }
		    	    }
	    	    if(res.get("/people/person/date_of_birth")!=null){
	    	    	String from = new MatrixCreator().stemDate(res.get("/people/person/date_of_birth").toString());

			    	   bw.write(subject+","+"/people/person/date_of_birth"+","+from+"\n" );

		    	    }
	    	    if(res.getJSONArray("/people/person/spouse_s")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/people/person/spouse_s").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/people/person/spouse_s").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/people/person/spouse_s/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/people/person/spouse_s/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/people/person/employment_history")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/people/person/employment_history").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/people/person/employment_history").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/people/person/employment_history/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/people/person/employment_history/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/people/person/education")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/people/person/education").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/people/person/education").getJSONObject(j);
		    	    	String from = rec.get("start_date").toString();
		    	    	String to = rec.get("end_date").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/people/person/education/start_date"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/people/person/education/end_date"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/people/person/places_lived")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/people/person/places_lived").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/people/person/places_lived").getJSONObject(j);
		    	    	String from = rec.get("start_date").toString();
		    	    	String to = rec.get("end_date").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/people/person/places_lived/start_date"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/people/person/places_lived/end_date"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/sports/pro_athlete/teams")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/sports/pro_athlete/teams").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/sports/pro_athlete/teams").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/sports/pro_athlete/teams/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/sports/pro_athlete/teams/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/sports/pro_athlete/sports_played_professionally")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/sports/pro_athlete/sports_played_professionally").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/sports/pro_athlete/sports_played_professionally").getJSONObject(j);
		    	    	String from = rec.get("career_start").toString();
		    	    	String to = rec.get("career_end").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/sports/pro_athlete/sports_played_professionally/career_start"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/sports/pro_athlete/sports_played_professionally/career_end"+","+to+"\n" );}

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

