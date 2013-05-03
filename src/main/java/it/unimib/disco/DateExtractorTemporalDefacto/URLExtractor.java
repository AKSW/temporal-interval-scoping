package it.unimib.disco.DateExtractorTemporalDefacto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import umontreal.iro.lecuyer.probdist.NormalDist;

class URLExtractor implements Runnable {
	URL _u;
	PrintWriter _pw;
	String _start,_end;
	
	public URLExtractor(URL u, PrintWriter pw,String start, String end) {
		_u = u;
		_pw = pw;
		_start=start;
		_end=end;
	}

	public void run() {
		HashSet<ArrayList<String>> facts = new HashSet<ArrayList<String>>();
		
		try {
			HttpURLConnection conn = (HttpURLConnection)_u.openConnection();
			conn.setConnectTimeout(10*100000);
			conn.setReadTimeout(10*100000);
			conn.addRequestProperty("User-Agent", "Data Quality bot (anisa.rula@disco.unimib.it)");
			
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			String jsonText=builder.toString();
		
			JSONObject json = new JSONObject(jsonText);
			
			Object subject = json.get("subject");
			Object predicate = json.get("predicate");
			Object object = json.get("object");
			JSONObject small = json.getJSONObject("small");
			//JSONObject medium = json.getJSONObject("medium");
			//JSONObject large = json.getJSONObject("large");
			@SuppressWarnings("rawtypes")
			Iterator it= small.keys();
			while(it.hasNext()){
								
				String timepoint = (String)it.next();
				
				ArrayList<String> fact= new ArrayList<String>();
				
				fact.add(subject.toString());
				fact.add(predicate.toString());
				fact.add(object.toString());
				fact.add(_start);
				fact.add(_end);
				fact.add(timepoint);
				fact.add(small.get(timepoint).toString());
				
				facts.add(fact);
				//_pw.println(fact);
			 }
			HashMap<String,Integer> resourceTotOccu= countOccurrence(facts);
			HashSet<ArrayList<String>> factsProb=frequProbabCalculator(facts,resourceTotOccu);
			//calculate the probability distribution
			//statisticalCalculator(facts);
			//factsProb= probabilityCalculator(statisticalCalculator(facts), facts);
			for (ArrayList<String> str: factsProb){
				_pw.println(str);
			}
			
			
			
			/*it= medium.keys();
			while(it.hasNext()){
				String key = (String)it.next();
				_pw.println(subject+","+predicate+","+object+","+_start +"," +_end+","+"medium"+","+key+","+medium.get(key));
			    
			 }
			it= large.keys();
			while(it.hasNext()){
				String key = (String)it.next();
				_pw.println(subject+","+predicate+","+object+","+_start +"," +_end+","+"large"+","+key+","+large.get(key));
			    
			 }*/

			//_pw.println(subject+" "+predicate+" "+object+" "+start +" " +end);
			
		} catch (Exception e) {
			System.err.println("Problem with " + _u + ": " + e.getMessage());
			e.printStackTrace();
		}
		
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
	
	@SuppressWarnings("static-access")
	public HashMap<String, ArrayList<String>>probabilityCalculator(HashMap<String,Double> statisticalElements,HashMap<String, ArrayList<String>> facts) {
		
		double mean=statisticalElements.get("mean"),sd=statisticalElements.get("sd"), probability=0.0;
		HashMap<String, ArrayList<String>> factsProb=new HashMap<String, ArrayList<String>>();
		
		NormalDist normdist= new NormalDist(mean,sd);
	
		for(String timepoint:facts.keySet()){
			ArrayList<String> factProb=facts.get(timepoint);
			double x = Integer.parseInt(facts.get(timepoint).get(6)) * 1.0;
			probability = normdist.cdf(mean,sd,x);
			factProb.add(7, String.valueOf(probability));
			factsProb.put(timepoint,factProb);
			
		}
		
		return factsProb;
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
