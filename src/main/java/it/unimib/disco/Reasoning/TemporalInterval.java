package it.unimib.disco.Reasoning;

import java.util.Date;

/**
 * A class to manipulate elements of an Allen algebra over a
 * an integer time space.
 *
 * See [James F. Allen. 
 *      Maintaining knowledge about temporal intervals. 
 *      Communications of the ACM, 26(11):832-843, 
 *      1983]
 */
public class TemporalInterval {

 private long start;
 private long end;

 public TemporalInterval(long start, long end) {
 	this.start=start;
 	this.end=end;
 }
 
 public TemporalInterval(Date start, Date end) {
 	this(start.getTime(), end.getTime());
 }
 
 public long getStart() { return start; }
 public long getEnd() { return end; }
 
 /**
  * This implements the = relation, fitting smoothly in Java 
  * object model.
  * As a deviance from standard algebra, this operator allows 
  * comparison with elements of different object spaces, 
  * returning always false - as conventional in JOMs.
  */
 public boolean equals(Object obj) {
 	if (! (obj instanceof TemporalInterval)) return false;
 	TemporalInterval interval = (TemporalInterval)obj;
 	return (start==interval.getStart() &&
 	        end==interval.getEnd());
 }
 
 /** 
  * The < relation
  */
 public boolean precedes(TemporalInterval ti) {
 	return end < ti.getStart();
 }
 
 /** 
  * The > relation
  */
 public boolean follows(TemporalInterval ti) {
 	return start > ti.getEnd();
 }
 
 /**
  * The "m" relation
  */
 public boolean meets(TemporalInterval ti) {
 	return end==ti.getStart();
 }
 
 /**
  * The "mi" relation
  */
 public boolean meetsInverse(TemporalInterval ti) {
 	return ti.getEnd()==start;
 }
 
 /**
  * The "d" relation
  */
 public boolean during(TemporalInterval ti) {
 	return start > ti.getStart() && end < ti.getEnd();
 }
 
 /**
  * The "di" relation
  */
 public boolean duringInverse(TemporalInterval ti) {
 	return start < ti.getStart() && end > ti.getEnd();	
 }
 
 /**
  * The "s" relation
  */
 public boolean starts(TemporalInterval ti) {
 	return start == ti.getStart() && end < ti.getEnd();
 }
 
 /**
  * The "si" relation
  */
 public boolean startsInverse(TemporalInterval ti) {
 	return start == ti.getStart() && end > ti.getEnd();
 }
 
 /**
  * The "f" relation
  */
 public boolean finishes(TemporalInterval ti) {
 	return start > ti.getStart() && end == ti.getEnd();
 }
 
 /**
  * The "fi" relation
  */
 public boolean finishesInverse(TemporalInterval ti) {
 	return start < ti.getStart() && end == ti.getEnd();
 }  
 
 /**
  * The "o" relation
  */
 public boolean overlaps(TemporalInterval ti) {
 	long tiStart = ti.getStart();
 	return start < tiStart && end > tiStart && end < ti.getEnd();
 }
 
 /**
  * The "oi" relation
  */
 public boolean overlapsInverse(TemporalInterval ti) {
 	long tiEnd = ti.getEnd();
 	return end > ti.getStart() && start < tiEnd && end > tiEnd;
 }  
}