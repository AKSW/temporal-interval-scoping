package it.unimib.disco.FactExtractor;

public class DateOccurrence {

	private String date;
	private String pred;
	private String occurrence;
	
	public DateOccurrence(){
		this.date = "";
		this.pred = "";
		this.occurrence=null;
	}
	
	public DateOccurrence(String initialDate, String initialPred, String initialOccurrence){
		this.date = initialDate;
		this.pred = initialPred;
		this.occurrence = initialOccurrence;
	}
	
	public DateOccurrence(String initialDate, String initialOccurrence){
		this.date = initialDate;
		this.occurrence = initialOccurrence;
	}
	public DateOccurrence(String initialDate){
		this.date = initialDate;

	}
	public String getDate(){

		return date;
	}
	
	public String getPred(){

		return pred;
	}
	
	public String getOccurrence(){

		return occurrence;
	}
	
	public void writeOutput(){

		System.out.print(date);
		System.out.print(" ");

		System.out.print(occurrence);
		System.out.print(" ");

		
	}
}
