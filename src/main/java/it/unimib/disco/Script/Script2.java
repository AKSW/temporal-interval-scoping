package it.unimib.disco.Script;

import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.aksw.distributions.Fact;

public class Script2 {
	public static void main (String args []) throws FileNotFoundException{
		if (args.length < 1) {
			System.out.println("Use: java TemporalIntervalCreator <Resource list file> <temporal defacto output> <yago's gold standard>");
			System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList_in2.txt /sortbyplayer-labels-with-space_out_lionel.csv /goldStandard_30_entities.csv");
		} else {
			
			HashMap<String,HashMap<String,HashMap<String,ArrayList<Double>>>> outputResult = new HashMap<String,HashMap<String,HashMap<String,ArrayList<Double>>>>();
			HashMap<String,HashMap<String,ArrayList<Double>>> evaluationResult= new HashMap<String,HashMap<String,ArrayList<Double>>>();

		
		//TemporalIntervalCreatoScriptTD tempAnnot= new TemporalIntervalCreatoScriptTD();
		
		TemporalIntervalCreatoScript tempAnnot= new TemporalIntervalCreatoScript();
	
		// Resource URI extraction
		List<String> resURIs = ReadFiles.getURIs(new File(args[0]));
		HashMap<String,ArrayList<String>> dateRepository=new HashMap<String,ArrayList<String>>();
		dateRepository=new ReadFiles().readCSVFile(resURIs);
		
		
		//logger.info("DBpedia date repository ");
				
		// Read temporalDefacto facts
		List<String> temporalDefactoFacts = ReadFiles.getURIs(new File(args[1]));
		List<Fact> l = new ArrayList<Fact>();
		l = new ReadFiles().creatListOfFacts(temporalDefactoFacts);
		//logger.info("TemporalDefacto facts parsed");
				
				
		//Read gold standard facts
		List<String> yagoFacts = ReadFiles.getURIs(new File(args[2]));
		//logger.info("Yago facts parsed");
				
		int normalization = 0; 
		
		int selection=1; //default top-k 
		int k=0,x=0;
		Scanner read = new Scanner(System.in); 
		
		do{
			System.out.println("Please choose one of the selection function: 1- topK, 2- proxy, 3 - neighbor:");
			selection=read.nextInt();
		}while(selection < 1  && selection>3);
		
		//selection function top-k
		if(selection==1){
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, HashMap<String,HashMap<String,ArrayList<Double>>>>();
			
			System.out.println("Selection function is topK");
			do{
				System.out.println("Please insert a positiv integer k from 1 to 2:");
				k=read.nextInt();
			}while(k<=0||k>3);
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				k=read.nextInt();
			}while(normalization<=0||normalization>4);
			
			
			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);
				

			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}
		}
		
		
		//selection function proxy
		else if(selection==2){
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, HashMap<String,HashMap<String,ArrayList<Double>>>>();
			
			System.out.println("You selected the proxy selection function");
			do{
				System.out.println("Please insert a positiv integer x from 1 to 10:");
				x=read.nextInt();
			}while(x<=0||x>10);
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				k=read.nextInt();
			}while(normalization<=0||normalization>4);

			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);

			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}
			


		}
		//selection function combined
		else{ 
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, HashMap<String,HashMap<String,ArrayList<Double>>>>();
			
			System.out.println("You selected neighbor function");
			do{
				System.out.println("Please insert a positiv integer for k from 1 to 2:");
				k=read.nextInt();
				read.nextLine();
				System.out.println("Please insert a positiv integer for x from 1 to 10:");
				x=read.nextInt();
			}while((k <= 0||k > 2) && (x <= 0||x>10));
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				k=read.nextInt();
			}while(normalization<=0||normalization>4);
			
			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);

			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(dateRepository,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}

		}
		
	
		try {
			BufferedWriter bw;
			File directory = new File (".");
			if(selection==1){
				
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_topK"+"-"+k+"-"+x+".csv")));
			}
			else if(selection==2){
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_proxyX"+"-"+k+"-"+x+".csv")));
			}
			else{
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_neighbor"+"-"+k+"-"+x+".csv")));
			}
			
			bw.write("normalizationType"+","+"overlapPrec"+","+"overlapRecall"+","+"Micro-F1"+","+"Macro-F1"+"\n" );
			for(String str: outputResult.keySet()){
				HashMap<String,HashMap<String,ArrayList<Double>>> result= new HashMap<String,HashMap<String,ArrayList<Double>>>();
				result=	 outputResult.get(str);

				double avgP=0d, avgR=0d, avgF=0d;
				int total=0;
				for (String uri:result.keySet()){
				
				HashMap<String,ArrayList<Double>> er=result.get(uri);
				for (String obj: er.keySet()){
					ArrayList<Double> metrics = er.get(obj);

					if(metrics.get(0).isNaN()||metrics.get(1).isNaN()||metrics.get(2).isNaN()){
						//System.out.println(uri+" "+obj+" "+metrics.get(0) +" "+ metrics.get(1)+" "+metrics.get(2));
					}
					else{
						avgP=avgP+metrics.get(0);
						avgR=avgR+metrics.get(1);
						avgF=avgF+metrics.get(2);
					
					total++;
					}
				}
			}
				
				double overlapPrec=avgP/total;
				double overlapRec=avgR/total;
				double microF1=avgF/total;
				double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
			
			bw.write(str+","+overlapPrec+","+overlapRec+","+microF1+","+macroF1+"\n" );

			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	}
}
