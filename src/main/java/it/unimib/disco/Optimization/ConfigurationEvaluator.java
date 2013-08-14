package it.unimib.disco.Optimization;

import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class ConfigurationEvaluator implements Evaluator<Configuration> {

	public Objectives evaluate(Configuration phenotype) {
		int value = 0;
		for (int i = 0; i < phenotype.length(); i++) {
			
		}
		Objectives objectives = new Objectives();
		objectives.add("objective", Sign.MAX, value);
		return objectives;
	}
}
