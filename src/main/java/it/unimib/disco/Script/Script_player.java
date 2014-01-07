package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.ReadFiles.FactGrouping;
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

public class Script_player {
	public static void main (String args []) throws FileNotFoundException{
		if (args.length < 1) {
			System.out.println("Use: java TemporalIntervalCreator <Resource list file> <temporal defacto output> <yago's gold standard>");
			System.out.println("Example: java TemporalIntervalCreator /temporalIntervalCreator_ResourceList_in2.txt /sortbyplayer-labels-with-space_out_lionel.csv /goldStandard_30_entities.csv");
		} else {
			
			HashMap<String,List<QualityMeasure>> outputResult = new HashMap<String,List<QualityMeasure>>();
			List<QualityMeasure> evaluationResult= new ArrayList<QualityMeasure>();

		
			TemporalIntervalCreatoScript tempAnnot= new TemporalIntervalCreatoScript();
			
			// Resource URI extraction
			List<Fact> dateRepository=new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
			HashMap<String,HashMap<String,List<Fact>>> groupedFactBySubjectObject = new FactGrouping().groupBySubjectObject(dateRepository); //group temporal facts (s,p,t) by subject and object (t)
					

			// Read temporalDefacto facts
			List<Fact> l = new ReadFiles().readTabSeparatedFileLS(new File(args[1]));
								
					
			//Read gold standard facts
			List<String> yagoFacts = ReadFiles.getURIs(new File(args[2]));
			
				
		//2, 10, 1, 1
		//[1, 6, 2, 1]
		int normalization = 1; 
		int selection=2; //default top-k 
		int k=1,x=10;
		Scanner read = new Scanner(System.in); 
		
		do{
			System.out.println("Please choose one of the selection function: 1- topK, 2- proxy, 3 - neighbor:");
			selection=read.nextInt();
		}while(selection < 1  && selection > 3);
		
		//selection function top-k
		if(selection==1){
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, List<QualityMeasure>>();
			
			System.out.println("Selection function is topK");
			do{
				System.out.println("Please insert a positiv integer k from 1 to 2:");
				k=read.nextInt();
			}while(k<=0||k>3);
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				normalization=read.nextInt();
			}while(normalization<=0||normalization>4);
			
			
			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);
			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}
		}
		
		
		//selection function proxy
		else if(selection==2){
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, List<QualityMeasure>>();
			
			System.out.println("You selected the proxy selection function");
			do{
				System.out.println("Please insert a positiv integer x from 1 to 10:");
				x=read.nextInt();
			}while(x<=0||x>10);
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				normalization=read.nextInt();
			}while(normalization<=0||normalization>4);

			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);

			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}
			


		}
		//selection function combined
		else{ 
			normalization = 1;//no-normalization
			outputResult = new HashMap<String, List<QualityMeasure>>();
			
			System.out.println("You selected neighbor function");
			do{
				System.out.println("Please insert a positiv integer for k from 1 to 2:");
				k=read.nextInt();
			}while((k <= 0||k > 2));
			do{
				System.out.println("Please insert a positiv integer for x from 1 to 10:");
				x=read.nextInt();
			}while( (x <= 0||x>10));
			
			System.out.println("Insert a normalization function");
			do{
				System.out.println("Please insert a positiv integer 1-no normalization, 2-local normalization, 3-global normalization, 4- chisquare normalization:");
				normalization=read.nextInt();
			}while(normalization<=0||normalization>4);
			
			//default no-normalization
			if (normalization==1){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("no-normalization",evaluationResult);

			}

			//default local-normalization
			else if (normalization==2){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("local-normalization",evaluationResult);

			}
			//default global-normalization
			else if(normalization==3){
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("global-normalization",evaluationResult);

			}
			//default chisquared-normalization
			else{
				evaluationResult = tempAnnot.temporalFact(groupedFactBySubjectObject,l,yagoFacts,normalization,selection,k,x);
				outputResult.put("chisquared-normalization",evaluationResult);
			}

		}
		
	
		try {
			BufferedWriter bw;
			File directory = new File (".");
			if(selection==1){
				
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_topK_player"+"-"+k+"-"+x+".csv")));
			}
			else if(selection==2){
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_proxyX_player"+"-"+k+"-"+x+".csv")));
			}
			else{
				bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_neighbor_player"+"-"+k+"-"+x+".csv")));
			}
			
			bw.write("subject"+"	"+"object"+"	"+"interval"+"	"+"goldstandard"+"	"+"precision"+"	"+"recall"+"	"+"microF"+"	"+"macroF"+"\n" );
			for(String str: outputResult.keySet()){
				List<QualityMeasure> result= new ArrayList<QualityMeasure>();
				result = outputResult.get(str);

				double avgP=0d, avgR=0d, avgF=0d;
				int total=0;
				for (QualityMeasure metrics:result){
					
					double prec = Double.parseDouble(metrics.get(QualityMeasure.Entry.PRECISION));
					double rec = Double.parseDouble(metrics.get(QualityMeasure.Entry.RECALL));
					double fmes = Double.parseDouble(metrics.get(QualityMeasure.Entry.fMEASURE));
					
					bw.write(metrics.get(QualityMeasure.Entry.SUBJECT)+"	"+metrics.get(QualityMeasure.Entry.OBJECT)+"	"+metrics.get(QualityMeasure.Entry.INTERVAL)+"	"+metrics.get(QualityMeasure.Entry.GOLDSTANDARD)+"	"+prec+"	"+rec+"	"+fmes+"	"+" "+"\n" );
					if(Double.isNaN(prec)||Double.isNaN(rec)||Double.isNaN(fmes)){
					}
					else{
						avgP=avgP+prec;

						avgR=avgR+rec;
						avgF=avgF+fmes;
					
					total++;
					}
				}
		
				
			
				double overlapPrec=avgP/total;
				double overlapRec=avgR/total;
				double microF1=avgF/total;
				double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
			
			bw.write(" "+"	"+str+"	"+" "+"	"+" "+"	"+overlapPrec+"	"+overlapRec+"	"+microF1+"	"+macroF1+"\n" );
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
