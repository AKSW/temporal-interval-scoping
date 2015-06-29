package it.unimib.disco.Utilities;

import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.Reasoning.Interval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class WriteOutput {

	private BufferedWriter bw;

	public void writeEvaluationOutput(List<QualityMeasure> evaluationResults) {

		try {

			File directory = new File (".");

			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_player_MWBM.csv")));
			bw.write("subject"+"	"+"object"+"	"+"interval"+"	"+"goldstandard"+"	"+"precision"+"	"+"recall"+"	"+"microF"+"	"+"macroF"+"\n" );

			double avgP=0d, avgR=0d, avgF=0d;
			int total=0;

			for (QualityMeasure metrics:evaluationResults){

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

			bw.write(" "+"	"+" "+"	"+" "+"	"+overlapPrec+"	"+overlapRec+"	"+microF1+"	"+macroF1+"\n" );

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIntervals(HashMap<String,HashMap<String,List<Interval>>> matrixIntervals) {

		try {

			File directory = new File (".");

			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/interval/"+"intervals")));
			//bw.write("subject"+"	"+"object"+"	"+"intervals"+"	"+"approachA"+"	"+"approachB"+"	"+"goldstandard"+"	"+"microF"+"\n" );
			bw.write("subject"+"	"+"object"+"	"+"intervals"+"\n" );


			for (String uri:matrixIntervals.keySet()){
				for (String obj:matrixIntervals.get(uri).keySet()){
					bw.write(uri+"	"+obj+"	"+matrixIntervals.get(uri).get(obj)+"\n" );
				}
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIntervalsForStatistics(HashMap<String,HashMap<String,List<Interval>>> matrixIntervals, String output) {

		try {

			File directory = new File (".");

			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/interval/"+output)));

			for (String uri:matrixIntervals.keySet()){
				for (String obj:matrixIntervals.get(uri).keySet()){
					for (int i=0; i<matrixIntervals.get(uri).get(obj).size(); i++){

						bw.write(uri+"	"+obj
								+"	"+matrixIntervals.get(uri).get(obj).get(i).getStart()
								+"	"+matrixIntervals.get(uri).get(obj).get(i).getEnd()
								+"	"+matrixIntervals.get(uri).get(obj).get(i).getValue()+"\n" );

					}
				}
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIntervalsReasoning(HashMap<String,HashMap<String,HashSet<Interval>>> intervals, String output) {

		try {

			File directory = new File (".");

			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/interval/"+output)));

			for (String uri:intervals.keySet()){
				for (String obj:intervals.get(uri).keySet()){
					Iterator<Interval> setIterator = intervals.get(uri).get(obj).iterator();
					while(setIterator.hasNext()){
						Interval item = setIterator.next();
						bw.write(uri+"	"+obj
								+"	"+item.getStart()
								+"	"+item.getEnd()+"\n" );

				}
			}
		}

		bw.flush();
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void writeComposedResult(HashSet<List<String>> f1, HashSet<List<String>> f2, HashSet<List<String>> f3) {

	try {

		File directory = new File (".");

		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/composedResult.tsv")));
		bw.write("subject"+"	"+"object"+"	"+"originalApproach"+"	"+"MWBMapproach"+"	"+"goldstandard"+"	"+"microF_orignialApproach"+"	"+"microF_MWBMapproach"+"\n" );

		Iterator<List<String>> itr2 = f2.iterator();
		while(itr2.hasNext())
		{
			List<String> lst2= itr2.next();

			Iterator<List<String>> itr3 = f3.iterator();
			while(itr3.hasNext())
			{
				List<String> lst3= itr3.next();

				if(lst3.get(0).equalsIgnoreCase(lst2.get(0))&&(lst3.get(1).equalsIgnoreCase(lst2.get(1)))){
					Iterator<List<String>> itr1 = f1.iterator();
					while(itr1.hasNext())
					{
						List<String> lst1= itr1.next();
						if(lst1.get(0).equalsIgnoreCase(lst3.get(0))&&(lst1.get(1).equalsIgnoreCase(lst3.get(1)))){

							bw.write(lst3.get(0)+"	"+lst3.get(1)+"	"+lst2.get(2)+"	"+lst3.get(2)+"	"+lst3.get(3)+"	"+lst2.get(6)+"	"+lst3.get(6)+"\n" );
						}
					}
				}
			}
		}



		bw.flush();
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void main(String args []) throws IOException{
	//lettura files
	File file1 = new File("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/interval/intervals");
	HashSet<List<String>> f1 = new ReadFiles().readTSV(file1);
	File file2 = new File("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/evaluation_player.csv");
	HashSet<List<String>> f2 = new ReadFiles().readTSV(file2);
	File file3 = new File("/Users/anisarula/Documents/git/temporal-interval-scoping.git/output/evaluation_player_MWBM.csv");
	HashSet<List<String>> f3 = new ReadFiles().readTSV(file3);

	WriteOutput w = new WriteOutput();
	w.writeComposedResult(f1, f2, f3);
}
}
