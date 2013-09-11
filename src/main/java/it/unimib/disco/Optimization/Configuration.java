package it.unimib.disco.Optimization;

import java.util.HashSet;
import java.util.Set;


public class Configuration {
	
	public int selection;
	public int x;
	public int k;
	public int normalization;
	
	public Set<Configuration> configurations = new HashSet<Configuration>();
	
	public Configuration(int selection, int x, int k, int normalization){
		this.selection=selection;
		this.x=x;
		this.k=k;
		this.normalization=normalization;

	}
	public Configuration(int size) {

		for (int i = 0; i < size; i++){ 
			selection = 1 + (int)(Math.random() * ((3 - 1) + 1));
			x = 1 + (int)(Math.random() * ((10 - 1) + 1));
			k = 1 + (int)(Math.random() * ((2 - 1) + 1));
			normalization = 1 + (int)(Math.random() * ((4 - 1) + 1));
			final Configuration configuration = new Configuration(selection, x, k, normalization);
			configurations.add(configuration);
		}
	}
	public int getSelection(){
		
		return selection;
	}
	public int getNormalization(){
		
		return normalization;
	}
	public int getX(){
	
		return x;
	}
	public int getK(){
		
		return k;
	}
	
	
	
	
	public Configuration[] getArrayConfigurations(){
		Configuration configurations_array [] = configurations.toArray(new Configuration[configurations.size()]);
		return configurations_array;
	}
	
	public Set<Configuration> getConfigurations(){
		return configurations;
	}
	
	public String toString()
    {
        return configurations.toString();
    }
}
