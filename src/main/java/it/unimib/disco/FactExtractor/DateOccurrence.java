package it.unimib.disco.FactExtractor;

import java.util.ArrayList;

public class DateOccurrence {

	private String date;
	private String pred;
	private ArrayList<String> occurrence;
	
	public DateOccurrence(){
		this.date = "";
		this.pred = "";
		this.occurrence=null;
	}
	
	public DateOccurrence(String initialDate, String initialPred, ArrayList<String> initialOccurrence){
		this.date = initialDate;
		this.pred = initialPred;
		this.occurrence = initialOccurrence;
	}
	
	public DateOccurrence(String initialDate, ArrayList<String> initialOccurrence){
		this.date = initialDate;
		this.occurrence = initialOccurrence;
	}
	public String getDate(){

		return date;
	}
	
	public String getPred(){

		return pred;
	}
	
	public ArrayList<String> getOccurrence(){

		return occurrence;
	}
	
	public void writeOutput(){

		System.out.print(date);
		System.out.print(" ");

		System.out.print(occurrence);
		System.out.print(" ");

		
	}
}
