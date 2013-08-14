package it.unimib.disco.Optimization;

import java.util.HashSet;
import java.util.Set;

import org.opt4j.core.start.Constant;


public class Configuration {
	
	private int selection;
	private int x;
	private int k;
	private int normalization;
	
	protected Set<Configuration> configurations = new HashSet<Configuration>();
	
	public Configuration(int selection, int x, int k, int normalization){
		this.selection=selection;
		this.x=x;
		this.k=k;
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
	
	
	
	public Configuration(@Constant(value = "size") int size) {

		for (int i = 0; i < size; i++) {
			selection = 1 + (int)(Math.random() * ((3 - 1) + 1));
			normalization = 1 + (int)(Math.random() * ((4 - 1) + 1));
			x = 1 + (int)(Math.random() * ((3 - 1) + 1));
			k = 1 + (int)(Math.random() * ((10 - 1) + 1));

			final Configuration configuration = new Configuration(selection, x, k, normalization);
			configurations.add(configuration);
		}
	}
	
	
	public Set<Configuration> getConfigurations(){
		return configurations;
	}
}
