package it.unimib.disco.Optimization;

import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.Evaluation.QualityMeasure.Entry;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.aksw.distributions.Fact;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class ConfigurationEvaluator implements Evaluator<Configuration> {

	List<String> yagoFacts;
	HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject = new HashMap<String,HashMap<String,List<Fact>>>();
	List<Fact> temporalDefactoFacts;
	
	public ConfigurationEvaluator (HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject, List<Fact> temporalDefactoFacts, List<String> yagoFacts){
		this.groupedFactBySubjectObject = groupedFactBySubjectObject;
		this.temporalDefactoFacts = temporalDefactoFacts;
		this.yagoFacts = yagoFacts;
	}
	
	public Objectives evaluate(Configuration phenotype) {
		
		double microF = 0d;

		TemporalIntervalCreator interv = new TemporalIntervalCreator();
		QualityMeasure m = new QualityMeasure();
		try {
		
			m = interv.measure(phenotype, groupedFactBySubjectObject, temporalDefactoFacts, yagoFacts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		microF = Double.parseDouble(m.get(Entry.fMEASURE));
		Objectives objectives = new Objectives();
		Objective objective = new Objective("maximize", Sign.MAX);
		
		objectives.add(objective,microF);
		return objectives;
	}
}
