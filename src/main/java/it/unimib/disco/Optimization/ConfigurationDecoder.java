package it.unimib.disco.Optimization;

import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Decoder;

public class ConfigurationDecoder implements Decoder<SelectGenotype<Configuration>,Configuration >{

	public Configuration decode(SelectGenotype<Configuration> genotype) {
		Configuration phenotype = new Configuration(genotype.get(0),genotype.get(1),genotype.get(2),genotype.get(3));
		return phenotype;
	}

	


}
