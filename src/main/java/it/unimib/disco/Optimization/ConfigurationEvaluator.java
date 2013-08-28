package it.unimib.disco.Optimization;

import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.Evaluation.QualityMeasure.Entry;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aksw.distributions.Fact;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class ConfigurationEvaluator implements Evaluator<Configuration> {

	List<String> yagoFacts;
	HashMap<String,ArrayList<String>> resURIs = new HashMap<String,ArrayList<String>>();
	List<Fact> temporalDefactoFacts;
	
	public ConfigurationEvaluator (HashMap<String,ArrayList<String>> resURIs, List<Fact> temporalDefactoFacts, List<String> yagoFacts){
		this.resURIs = resURIs;
		this.temporalDefactoFacts = temporalDefactoFacts;
		this.yagoFacts = yagoFacts;
	}
	
	public Objectives evaluate(Configuration phenotype) {
		
		double microF = 0d;

		TemporalIntervalCreator interv = new TemporalIntervalCreator();
		QualityMeasure m = new QualityMeasure();
		try {
			m = interv.measure(phenotype, resURIs, temporalDefactoFacts, yagoFacts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		microF = m.get(Entry.fMEASURE);
		Objectives objectives = new Objectives();
		Objective objective = new Objective("maximize", Sign.MAX);
		
		objectives.add(objective,microF);
		return objectives;
	}
}
