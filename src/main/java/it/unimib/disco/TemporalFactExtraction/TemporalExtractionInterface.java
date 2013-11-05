package it.unimib.disco.TemporalFactExtraction;

import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;


public interface TemporalExtractionInterface {

	HashSet<Fact> extraction(List<String> resURIs);  

}
