package it.unimib.disco.Optimization;

import java.util.List;
import java.util.Random;

import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Creator;

public class ConfigurationCreator implements Creator<SelectGenotype<Configuration>>{
	//normalization [1..4]	selection [1..3]	x [1..3]	k [1..10]

	Configuration configuration = new Configuration (10);
	public SelectGenotype<Configuration> create() {
		int size = configuration.getConfigurations().size();
		int item = new Random().nextInt(size); 
		int i = 0;
		
		SelectGenotype<Configuration> genotype = new SelectGenotype<Configuration>((Configuration[]) configuration.getConfigurations().toArray()  );
		
		for(Configuration obj : configuration.getConfigurations())
		{
		    if (i == item)
		       genotype.add(obj);
		    i = i + 1;
		}
			
			

			return genotype;

	}
}
