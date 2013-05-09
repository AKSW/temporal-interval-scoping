package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.ReadFiles.ReadFiles;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;
import org.aksw.distributions.FactReader;
import org.aksw.distributions.GlobalNormalization;

public class TemporalIntervalFactAssociator {

	public HashMap<String,DateOccurrence [][]> diagonalMatrixCreator(HashMap<String, DateOccurrence [][]> reducedMatrix2){
		
		HashMap<String,DateOccurrence [][]> diagonalMatrixURI = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();
		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] diagonalMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<diagonalMatrix.length; i++){
				for(int j=1; j<diagonalMatrix[i].length; j++){
					if(j>=i&&!(diagonalMatrix[i][j].getOccurrence().get(0).equalsIgnoreCase("X"))){
						occurrence = new ArrayList<String>();
						
						double value = (diagonalMatrix.length -j)*1.0/(diagonalMatrix.length-i); 
						occurrence.add(String.valueOf(value));
						diagonalMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						diagonalMatrix[i][j]= new DateOccurrence("", occurrence);
					}
				}
			}
			diagonalMatrixURI.put(Uri, diagonalMatrix);
		}
		
		return diagonalMatrixURI;
		
	}
	
	public HashMap<String,DateOccurrence [][]> matrixYearsDuration(HashMap<String, DateOccurrence [][]> reducedMatrix2){
		
		HashMap<String,DateOccurrence [][]> temporalDCMatrixURI = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();

		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] temporalDCMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<temporalDCMatrix.length; i++){
				for(int j=1; j<temporalDCMatrix[i].length; j++){
					
					if(j>=i){
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						Date column = mpc.stringToLong(temporalDCMatrix[0][j].getDate());
						Date row = mpc.stringToLong(temporalDCMatrix[i][0].getDate());
						long millisecDistance = column.getTime()-row.getTime();
						long dayDistance= millisecDistance/(1000 * 60 * 60 * 24);
						long yearDistance= dayDistance/365;
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(yearDistance));
						
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
				}
			}
			temporalDCMatrixURI.put(Uri, temporalDCMatrix);
		}
		
		return temporalDCMatrixURI;
		
	}
	

	public DateOccurrence [][] matrixManhattanDuration( DateOccurrence [][] mRed){
		
		ArrayList<String> occurrence = new ArrayList<String>();

			DateOccurrence [][] temporalDCMatrix= new DateOccurrence[mRed.length][mRed.length];
			
			occurrence.add("End");
			temporalDCMatrix[0][0]= new DateOccurrence("[Start]/", occurrence);

			
			for(int l=1; l<temporalDCMatrix.length; l++){
				temporalDCMatrix[l][0] = mRed[l][0];

			}
			
			
			for(int m=1; m<temporalDCMatrix.length; m++){
				temporalDCMatrix[0][m]= mRed[0][m];
			}

			
			for (int i=1; i<temporalDCMatrix.length; i++){
				int count=1;
				for(int j=1; j<temporalDCMatrix[i].length; j++){
		
					if(j>=i&&!(mRed[i][j].getOccurrence().get(0).equalsIgnoreCase("X"))){
						
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(count));
						count++;
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					
				}
	
			}
/*			for (int i=0; i<temporalDCMatrix.length;i++){
			for (int j=0;j<temporalDCMatrix[i].length;j++){
				
				System.out.print(temporalDCMatrix[i][j].getDate()+""+ temporalDCMatrix[i][j].getOccurrence()+";");
			}
			System.out.println();

	}*/
		return temporalDCMatrix;
		
	}


	public HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupOccuByYear(List<String> temporaldefacto){
		ReadFiles rf=new ReadFiles();
		HashSet<ArrayList<String>> fileNormalized=rf.readCommaSeparatedFile(temporaldefacto);
		//HashSet<List<String>> fileNormalized = normalizeFrequency(file);
		
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	
		HashMap<String,HashSet<ArrayList<String>>> yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
		
		HashSet<ArrayList<String>> yearOccs = new HashSet<ArrayList<String>>();
		
		for (List<String> record:fileNormalized){
			
			ArrayList<String> yO = new ArrayList<String>();
			//System.out.println(record.get(0)+" "+record.get(2));
			if (!res.containsKey(record.get(0))){
				yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
				res.put(record.get(0), yearOccu);
			}
			
			yearOccu=res.get(record.get(0));
			if(!yearOccu.containsKey(record.get(2))){
				
				yearOccs = new HashSet<ArrayList<String>>();
				yearOccu.put(record.get(2),yearOccs);
			}
			
			yearOccs=yearOccu.get(record.get(2));
			yO.add(record.get(5)); //timepoint
			yO.add(record.get(6)); //occurrence of timepoint
			
			yearOccs.add(yO);

			yearOccu.put(record.get(2),yearOccs);


			res.put(record.get(0), yearOccu);
		 }
		
	
		/*for ( String str: res.keySet()){
			HashMap<String, HashSet<ArrayList<String>>> hm = res.get(str);
			for (String obj: hm.keySet()){
				System.out.println(str+" "+ obj+" "+ hm.get(obj));
			}
		}
		*/
		return res;
	}
	
	//normalization function
	public HashSet<List<String>> normalizeFrequency(HashSet<ArrayList<String>> file){
		HashSet<List<String>> normalizedFile = new HashSet<List<String>>();
		List<Fact> facts = new ArrayList<Fact>();
		
		for (List<String> f:file){
			Fact fact = FactReader.readFact(f);
			
	        facts.add(fact);

        }
        facts = (new GlobalNormalization()).normalize(facts);
			
       
        for (Fact f:facts){
        	normalizedFile.add(FactReader.listFromFact(f));
        }
		

		return normalizedFile;
	}
	
	public HashSet<ArrayList<String>> dcCalculator(HashSet<ArrayList<String>> yearOccu,DateOccurrence [][] matrixMD){
			//PrintWriter pw){
		
	
		ArrayList<String> occurrence = new ArrayList<String>();

			
			DateOccurrence [][] m = new DateOccurrence[matrixMD.length][matrixMD.length];
			HashSet<ArrayList<String>> intervals =new HashSet<ArrayList<String>>();
			for (int i=0; i<m.length; i++){
				
				for(int j=0; j<m[i].length; j++){
					ArrayList<String> interval = new ArrayList<String>();
					if(i==0||j==0){
						m[0][j]=matrixMD[0][j];
						m[i][0]=matrixMD[i][0];
					}
					
					else if(j>i&&!(matrixMD[i][j].getOccurrence().get(0).contains("0"))){
						
						String occuMatrix = matrixMD[i][j].getOccurrence().get(0).trim();
						int duration = Integer.valueOf(occuMatrix);
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						
						Date column = mpc.stringToLong(m[0][j].getDate());
						Date row = mpc.stringToLong(m[i][0].getDate());
						
						
						//double formula=hitCount(row,column,yearOccu);
						
						double hit=hitCount(row,column,yearOccu);
						double formula= hit/duration; 
						
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(formula));
						
						m[i][j]= new DateOccurrence("", occurrence);
					
						//add start and end year and the formula
						interval.add(m[i][0].getDate());
						interval.add(m[0][j].getDate());
						interval.add(String.valueOf(formula));
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						m[i][j]= new DateOccurrence("", occurrence);
					}
					if(interval.size()!=0){
					
						intervals.add(interval);
					
					}

					//pw.print(m[i][j].getDate()+""+ m[i][j].getOccurrence()+";");
					
				}//pw.println();
				
			}


		return intervals;
		
	}
	
	public double hitCount(Date row, Date column,HashSet<ArrayList<String>> yearOccu ){
		double hit=0;
		MatrixPruningCreator mpc= new MatrixPruningCreator();
		int count=0;
		for (ArrayList<String> value: yearOccu){

	            Date year= mpc.stringToLong(value.get(0));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            	hit=hit+Double.valueOf(value.get(1).trim());
	            	count++;
			
	            }
		}
		if (count>0){
		hit=hit/count;
		}

		return hit;
		
	}
	
	public int hitWeighted(Date row, Date column,HashSet<ArrayList<String>> yearOccu ){
		int hit=0;
		MatrixPruningCreator mpc= new MatrixPruningCreator();
		for (ArrayList<String> value: yearOccu){

	            Date year= mpc.stringToLong(value.get(0));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            	hit=hit+Integer.valueOf(value.get(1).trim());
			
	            }
		}

		return hit;
		
	}
}
