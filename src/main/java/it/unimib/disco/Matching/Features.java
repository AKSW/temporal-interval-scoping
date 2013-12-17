package it.unimib.disco.Matching;

public class Features {
	 private String predicate;
	 private String timepoint;
	 
	public Features(String predicate, String timepoint) {
	 	this.predicate=predicate;
	 	this.timepoint=timepoint;
	 }
	 
 
	 public String getPredicate() { return predicate; }
	 public String getTimepoint() { return timepoint; }
}
