package it.unimib.disco.Optimization;

import java.util.HashSet;
import java.util.Set;


public class Configuration {
	
	private int selection;
	private int x;
	private int k;
	private int normalization;
	
	protected Set<Configuration> configurations = new HashSet<Configuration>();
	
	public Configuration(int selection, int k, int x, int normalization){
		this.selection=selection;
		this.k=k;
		this.x=x;
		this.normalization=normalization;
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
	
	
	
	public Configuration(int size) {

		for (int i = 0; i < size; i++){ 
			selection = 1 + (int)(Math.random() * ((3 - 1) + 1));
			normalization = 1 + (int)(Math.random() * ((4 - 1) + 1));
			k = 1 + (int)(Math.random() * ((2 - 1) + 1));
			x = 1 + (int)(Math.random() * ((10 - 1) + 1));
			final Configuration configuration = new Configuration(selection, k, x, normalization);
			configurations.add(configuration);
		}
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
