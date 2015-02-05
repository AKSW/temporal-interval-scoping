package it.unimib.disco.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ConfigurationReader {

	public Configuration readConfiguration(File rsListFile) {
		Configuration config = new Configuration();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "	";

		try {

			br = new BufferedReader(new FileReader(rsListFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] conf = line.split(cvsSplitBy);

				config.add(Configuration.Entry.SELECTION, Integer.parseInt(conf[0]));
				config.add(Configuration.Entry.TOPK, Integer.parseInt(conf[1]));
				config.add(Configuration.Entry.NORMALIZATION, Integer.parseInt(conf[2]));
				config.add(Configuration.Entry.PROXY, Integer.parseInt(conf[3]));

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		verifyConfiguration(config);
		return config;
	}
	
	
	
	public void verifyConfiguration(Configuration config){
		
		if(config.get(Configuration.Entry.SELECTION)==1){
			if(config.get(Configuration.Entry.TOPK)<1||config.get(Configuration.Entry.TOPK)>2){
				System.err.println("TOPK values are not in the allowed range [1,2]");
				System.exit(1);	
			}
			else{
				if(config.get(Configuration.Entry.NORMALIZATION)<1||config.get(Configuration.Entry.NORMALIZATION)>4){
					System.err.println("Normalization values are not in the allowed range [1,4]");
					System.exit(1);	
				}

			}
		}
		else if(config.get(Configuration.Entry.SELECTION)==2){
			if(config.get(Configuration.Entry.NORMALIZATION)<1||config.get(Configuration.Entry.NORMALIZATION)>4){
				System.err.println("Normalization values are not in the allowed range [1,4]");
				System.exit(1);	
			}
			else{
				if(config.get(Configuration.Entry.PROXY)<1||config.get(Configuration.Entry.PROXY)>10){
					System.err.println("PROXY values are not in the allowed range [1,10]");
					System.exit(1);	
				}
			}
		}
		else if (config.get(Configuration.Entry.SELECTION)==3){
			if(config.get(Configuration.Entry.TOPK)<1||config.get(Configuration.Entry.TOPK)>2){
				System.err.println("TOPK values are not in the allowed range [1,2]");
				System.exit(1);	
			}
			else{
				if(config.get(Configuration.Entry.NORMALIZATION)<1||config.get(Configuration.Entry.NORMALIZATION)>4){
					System.err.println("Normalization values are not in the allowed range [1,4]");
					System.exit(1);	
				}
				else{
					if(config.get(Configuration.Entry.PROXY)<1||config.get(Configuration.Entry.PROXY)>10){
						System.err.println("PROXY values are not in the allowed range [1,10]");
						System.exit(1);	
					}
				}
			}
		}
		else{
			System.err.println("Selection values are not in the allowed range [1,3]");
			System.exit(1);	
		}

	}
}
