package it.unimib.disco.DateExtractorTemporalDefacto;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

class URLExtractor {
	
	public PrintWriter smallContextWriter;
	public PrintWriter mediumContextWriter;
	public PrintWriter largeContextWriter;
	
	public URLExtractor() {
		
		try {
			smallContextWriter = new PrintWriter(new FileOutputStream("sortbyplayer-labels-with-space_out_small.csv", true));
			mediumContextWriter = new PrintWriter(new FileOutputStream("sortbyplayer-labels-with-space_out_medium.csv", true));
			largeContextWriter = new PrintWriter(new FileOutputStream("sortbyplayer-labels-with-space_out_large.csv", true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(URL url, String start, String end) {
		
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10*100000);
			conn.setReadTimeout(10*100000);
			conn.addRequestProperty("User-Agent", "Data Quality bot (anisa.rula@disco.unimib.it)");
			
			// read the json return value from the webservice
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = reader.readLine()) != null) builder.append(line);
			
			JSONObject json = new JSONObject(builder.toString());
			
			Object subject = json.get("subject");
			Object predicate = json.get("predicate");
			Object object = json.get("object");
			JSONObject small = json.getJSONObject("small");
			JSONObject medium = json.getJSONObject("medium");
			JSONObject large = json.getJSONObject("large");
			
			processData(small, subject, predicate, object, this.smallContextWriter, start, end);
			processData(medium, subject, predicate, object, this.mediumContextWriter, start, end);
			processData(large, subject, predicate, object, this.largeContextWriter, start, end);
		}
		catch (Exception e) {
			System.err.println("Problem with " + url + ": " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * best method name ever
	 * @param end 
	 * @param start 
	 */
	public void processData(JSONObject context, Object subject, Object predicate, Object object, PrintWriter writer, String start, String end){
		
		HashSet<ArrayList<String>> facts = new HashSet<ArrayList<String>>();
		
		@SuppressWarnings("rawtypes")
		Iterator it= context.keys();
		while(it.hasNext()){
							
			String timepoint = (String)it.next();
			
			ArrayList<String> fact= new ArrayList<String>();
			
			fact.add(subject.toString());
			fact.add(predicate.toString());
			fact.add(object.toString());
			fact.add(start);
			fact.add(end);
			fact.add(timepoint);
			try {
				fact.add(context.get(timepoint).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			facts.add(fact);
			//_pw.println(fact);
		 }
		HashMap<String,Integer> resourceTotOccu= countOccurrence(facts);
		HashSet<ArrayList<String>> factsProb=frequProbabCalculator(facts,resourceTotOccu);
		//calculate the probability distribution
		//statisticalCalculator(facts);
		//factsProb= probabilityCalculator(statisticalCalculator(facts), facts);
		for (ArrayList<String> str: factsProb){
			writer.println(str);
		}
		writer.flush();
	}
	
	
	public HashSet<ArrayList<String>> frequProbabCalculator(HashSet<ArrayList<String>> facts, HashMap<String,Integer> resourceTotOccu){
		HashSet<ArrayList<String>> factsProb=new HashSet< ArrayList<String>>();
		double prob=0.0;
		for(ArrayList<String> fact:facts){
			ArrayList<String> factProb=fact;
			int totOccu=resourceTotOccu.get(fact.get(0));
			double occ = Integer.parseInt(fact.get(6)) * 1.0;
			prob=occ/totOccu;
			factProb.add(7, String.valueOf(prob));
			factsProb.add(factProb);
		}
		
		return factsProb;
	}
	
	public HashMap<String,Integer> countOccurrence(HashSet<ArrayList<String>> file){
	
		HashMap<String,Integer> resource = new HashMap<String,Integer>();
		int totalOccu=0;
		for (ArrayList<String> record:file){

			if (!resource.containsKey(record.get(0))){

				totalOccu=0;
				resource.put(record.get(0), totalOccu);
				
			}
			
			int occ = Integer.valueOf(record.get(6).trim());
			totalOccu+=occ;
			resource.put(record.get(0), totalOccu);

		 }
		
		/*for ( String str: resource.keySet()){
			HashMap<String, Integer> hm = resource.get(str);
			for (String y: hm.keySet()){
				System.out.println(str+" "+ y+" "+ hm.get(y));
			}
		}*/
		return resource;
	}
	
	
	
	public HashMap<String,Double> statisticalCalculator(HashMap<String, ArrayList<String>> facts) {
		HashMap<String,Double> stat=new HashMap<String,Double>();
		ArrayList<Integer> occurrences = new ArrayList<Integer>();
		
		int sum=0;
		double count=0.0;
		int i=0;
		for (String timepoint: facts.keySet() ){
			
			int occ= Integer.parseInt(facts.get(timepoint).get(6).toString());
			sum= sum + occ;
			
			count++;
			
			occurrences.add(i,occ);
			i++;
		}
		
		double mean=sum/count; //calculate the mean
		
		double sd= sd(occurrences,mean); //calculate the standard deviation
		stat.put("mean", mean);
		stat.put("sd",sd);
		return stat;
	}
	
	 
	public double sd (List<Integer> a, double mean){
	        int sum = 0;
	 
	        for (Integer i : a)
	            sum += Math.pow((i - mean), 2);
	        return Math.sqrt( sum / ( a.size() - 1 ) ); // sample
	}
	
}
