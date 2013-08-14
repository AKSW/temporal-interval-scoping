package it.unimib.disco.Optimization;

import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Constant;


public class ConfigurationModule extends ProblemModule {
	@Constant(value = "size")
	protected int size = 10;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	protected void config() {
		bindProblem(ConfigurationCreator.class, ConfigurationDecoder.class, ConfigurationEvaluator.class);
	}
} 
