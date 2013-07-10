package it.unimib.disco.FactExtractor;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/**
 * Download n3 doc pages
 * 
 * @author rula
 *
 */


public class FetchPages {
	
	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getLogger(ResourceFetcher.class);
		List<String> resURIs = ReadFiles.getURIs(new File(args[0]));

		for (String resUri:resURIs){
			logger.info("Downloading ontology model for resource "+resUri);
			
			String n3Uri = resUri.replaceAll("resource", "data")+".n3";
			String r=n3Uri.substring(n3Uri.lastIndexOf('/')+1);
			
			File directory = new File (".");
			FileOutputStream fos = new FileOutputStream(directory.getAbsoluteFile()+"/resources-ontologies/"+r);
			
			PrintWriter pw = new PrintWriter(fos);

			Model myRawModel = FileManager.get().loadModel(n3Uri, "N3");
			myRawModel.write(pw,"N3");
			
			pw.close();
			fos.close();

		}
	}
}