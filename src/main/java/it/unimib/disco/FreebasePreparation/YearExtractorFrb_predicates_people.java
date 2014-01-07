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

public class YearExtractorFrb_predicates_people {
	  
	public static Properties properties = new Properties();
	  
	public static void main(String[] args) {
	    
		try {
	    	File directory = new File (".");
	    	properties.load(new FileInputStream(directory.getAbsolutePath()+"/src/main/resources/freebase.properties"));
	     
	      HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();


	      BufferedWriter bw;
		  bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/repositoryDatesFrb/"+"repositoryDates_people_predicate_frb.csv")));
	      List<String> ls_resources_frb = ReadFiles.getURIs(new File(args[0]));
	      
	      List<String> ls_resources_dbp = ReadFiles.getURIs(new File(args[1]));
	      
	      String subject = null;
	      for(int z=0;z<ls_resources_frb.size();z++){
	    	  subject=ls_resources_frb.get(z);
	    	 
	      //String query="[{  \"/type/object/id\":\""+ls_resources_frb.get(z)+"\",  \"/people/person/spouse_s\": [{    \"from\": null,    \"to\": null  }],  " +
	    //  "  \"/music/artist/active_start\":  null,  \"optional\": \"optional\" ," +
	      //		"  \"/music/artist/active_end\":  null,  \"optional\": \"optional\" ," +		
	    	//	  "  \"/music/group_member/membership\": [{    \"start\": null,    \"end\": null,    \"optional\": \"optional\"  }]}],"+
	    //  "\"/tv/tv_actor/starring_roles\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      //		"  \"/tv/tv_personality/tv_regular_appearances\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	  //    		"  \"/people/person/date_of_birth\": null,   }]";
	  //    		"  \"/people/person/employment_history\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	   //   		"  \"/people/person/education\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	    //  		"  \"/people/person/places_lived\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	      //		"  \"/theater/theater_actor/theater_roles\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      	//	"  \"/award/award_nominee/award_nominations\": [{    \"year\": null,    \"optional\": \"optional\"  }]," +
	      //		"\"/award/award_winner/awards_won\": [{    \"year\": null,    \"optional\": \"optional\"  }]," +
	      //		"  \"/celebrities/celebrity/sexual_relationships\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]";
	      		 
	      		
	      		
	      	//	"  \"/award/ranked_item\": [{    \"year\": null    \"optional\": \"optional\"  }]," +
	      		
	      String query = "[{\"/type/object/id\":\""+ls_resources_frb.get(z)+"\",\"/people/person/spouse_s\":[{\"from\":null,\"to\":null}]," +
	     "  \"/music/artist/active_start\":  null,  \"optional\": \"optional\" ," +
	    		  "  \"/music/artist/active_end\":  null,  \"optional\": \"optional\" ," +
	      "  \"/music/group_member/membership\": [{\"start\": null,\"end\": null,\"optional\": \"optional\"  }],"+
	    		  "\"/tv/tv_actor/starring_roles\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      "\"/tv/tv_personality/tv_regular_appearances\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	    		  "  \"/people/person/employment_history\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
   		"  \"/people/person/education\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	     		"  \"/people/person/places_lived\": [{    \"start_date\": null,    \"end_date\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/theater/theater_actor/theater_roles\": [{    \"from\": null,    \"to\": null,    \"optional\": \"optional\"  }]," +
	      		"  \"/award/award_nominee/award_nominations\": [{    \"year\": null,    \"optional\": \"optional\"  }]," +
	      		"\"/award/award_winner/awards_won\": [{    \"year\": null,    \"optional\": \"optional\"  }]," +
	    		  "\"/people/person/date_of_birth\": null}]";
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
	    	     if(!res.get("/music/artist/active_start").toString().contains("null")){
	    	    	String from = new MatrixCreator().stemDate(res.get("/music/artist/active_start").toString());

			    	   bw.write(subject+","+"/music/artist/active_start"+","+from+"\n" );

		    	    }
	    	     if(!(res.get("/music/artist/active_end")).toString().contains("null")){
	    	    	String from = new MatrixCreator().stemDate(res.get("/music/artist/active_end").toString());

			    	   bw.write(subject+","+"/music/artist/active_end"+","+from+"\n" );

		    	    }
	    	     
	    	      if(res.getJSONArray("/music/group_member/membership")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/music/group_member/membership").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/music/group_member/membership").getJSONObject(j);
		    	    	String from = rec.get("start").toString();
		    	    	String to = rec.get("end").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/music/group_member/membership/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/music/group_member/membership/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/tv/tv_actor/starring_roles")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/tv/tv_actor/starring_roles").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/tv/tv_actor/starring_roles").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/tv/tv_actor/starring_roles/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/tv/tv_actor/starring_roles/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    if(res.getJSONArray("/tv/tv_personality/tv_regular_appearances")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/tv/tv_personality/tv_regular_appearances").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/tv/tv_personality/tv_regular_appearances").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/tv/tv_personality/tv_regular_appearances/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/tv/tv_personality/tv_regular_appearances/to"+","+to+"\n" );}

		    	    }
		    	    }
	    	    
	    	    if(res.get("/people/person/date_of_birth")!=null){
	    	    	String from = new MatrixCreator().stemDate(res.get("/people/person/date_of_birth").toString());

			    	   bw.write(subject+","+"/people/person/date_of_birth"+","+from+"\n" );

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
	    	    if(res.getJSONArray("/award/award_nominee/award_nominations")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/award/award_nominee/award_nominations").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/award/award_nominee/award_nominations").getJSONObject(j);
		    	    	String from = rec.get("year").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());

		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/award/award_nominee/award_nominations/year"+","+from+"\n" );
		    	    	}
		    	    }
		    	    }
	    	     if(res.getJSONArray("/award/award_winner/awards_won")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/award/award_winner/awards_won").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/award/award_winner/awards_won").getJSONObject(j);
		    	    	String from = rec.get("year").toString();
		    	    	
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/award/award_winner/awards_won/year"+","+from+"\n" );}
		    	    	

		    	    }
		    	    }
	    	     
	    	   
	    	   
	    	     if(res.getJSONArray("/theater/theater_actor/theater_roles")!=null){
		    	    for(int j = 0; j < res.getJSONArray("/theater/theater_actor/theater_roles").length(); j++){
		    	    	JSONObject rec = res.getJSONArray("/theater/theater_actor/theater_roles").getJSONObject(j);
		    	    	String from = rec.get("from").toString();
		    	    	String to = rec.get("to").toString();
		    	    	from = new MatrixCreator().stemDate(from.toString());
		    	    	to = new MatrixCreator().stemDate(to.toString());
		    	    	if (!from.contains("null")){
		    	    	bw.write(subject+","+"/theater/theater_actor/theater_roles/from"+","+from+"\n" );}
		    	    	if (!to.contains("null")){
				    	bw.write(subject+","+"/theater/theater_actor/theater_roles/to"+","+to+"\n" );}

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

