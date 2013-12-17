package it.unimib.disco.Matching;

import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.File;
import java.util.List;


public class TestFactGrouping {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> features = ReadFiles.getURIs(new File(args[0]));
		
		new FactGrouping().featureRIM(features);

	}

}
