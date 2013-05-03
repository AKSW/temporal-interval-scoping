package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
	
	public HashMap<String,DateOccurrence [][]> matrixYearsDuration(HashMap<String, DateOccurrence [][]> reducedMatrix2, List<String> temporaldefacto){
		
		HashMap<String,DateOccurrence [][]> temporalDCMatrixURI = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();
		ReadFiles rf=new ReadFiles();
		rf.readCommaSeparatedFile(temporaldefacto);
		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] temporalDCMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<temporalDCMatrix.length; i++){
				for(int j=1; j<temporalDCMatrix[i].length; j++){
					
					if(j>=i){
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						Date column = mpc.stringToLong(temporalDCMatrix[0][j].getDate());
						Date row = mpc.stringToLong(temporalDCMatrix[i][0].getDate());
						long yearDistance = column.getTime()-row.getTime();
						
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
	
public HashMap<String,DateOccurrence [][]> matrixManhattanDuration(HashMap<String, DateOccurrence [][]> reducedMatrix2){
		
	
		HashMap<String,DateOccurrence [][]> temporalDCMatrixURI = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();
		
	
		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] temporalDCMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<temporalDCMatrix.length; i++){
				int count=0;
				for(int j=1; j<temporalDCMatrix[i].length; j++){
					
					if(j>=i&&!(temporalDCMatrix[i][j].getOccurrence().get(0).equalsIgnoreCase("X"))){
						
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

			temporalDCMatrixURI.put(Uri, temporalDCMatrix);
		}

		return temporalDCMatrixURI;
		
	}


	public HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> groupOccuByYear(List<String> temporaldefacto){
		ReadFiles rf=new ReadFiles();
		HashSet<ArrayList<String>> file=rf.readCommaSeparatedFile(temporaldefacto);
		
		HashMap<String,HashMap<String,HashSet<ArrayList<String>>>> res = new HashMap<String,HashMap<String,HashSet<ArrayList<String>>>>();
	
		HashMap<String,HashSet<ArrayList<String>>> yearOccu = new HashMap<String,HashSet<ArrayList<String>>>();
		
		HashSet<ArrayList<String>> yearOccs = new HashSet<ArrayList<String>>();
		
		for (ArrayList<String> record:file){
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
			yO.add(record.get(5));
			yO.add(record.get(6));
			
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
	
	public HashMap<String,DateOccurrence [][]> dcCalculator(String Uri,HashMap<String,HashSet<ArrayList<String>>> yearOccu,HashMap<String,DateOccurrence [][]> reducedMatrix2,
			PrintWriter pw){
		
		HashMap<String,DateOccurrence [][]> objMatri=new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();

		
		for (String obj: yearOccu.keySet()){
			pw.println(Uri+" "+obj);
			HashSet<ArrayList<String>> yearOccs = yearOccu.get(obj);
			
			HashMap<String,DateOccurrence [][]> matrixManhattanDurationUri = matrixManhattanDuration(reducedMatrix2);
			DateOccurrence [][] matrixMD = matrixManhattanDurationUri.get(Uri);


			for (int i=0; i<matrixMD.length; i++){
				for(int j=0; j<matrixMD[i].length; j++){
					if(i==0||j==0){
						
					}
					
					else if(j>i&&!(matrixMD[i][j].getOccurrence().get(0).contains("0"))){
						
						String occuMatrix = matrixMD[i][j].getOccurrence().get(0).trim();
						int duration = Integer.valueOf(occuMatrix);
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						
						Date column = mpc.stringToLong(matrixMD[0][j].getDate());
						Date row = mpc.stringToLong(matrixMD[i][0].getDate());
						
						int hit=hitCalculator(row,column,yearOccs);

						double formula= hit*1.0/duration; 
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(formula));
						
						matrixMD[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						matrixMD[i][j]= new DateOccurrence("", occurrence);
					}
					pw.print(matrixMD[i][j].getDate()+""+ matrixMD[i][j].getOccurrence()+";");
					
				}pw.println();
				
			}
			
			objMatri.put(obj, matrixMD);
		}
		
		for ( String obj: objMatri.keySet()){
			DateOccurrence [][] hm = objMatri.get(obj);
			System.out.print(Uri+" "+obj);
			for (int i=1; i<hm.length; i++){
				for(int j=1; j<hm[i].length; j++){
					System.out.print(hm[i][j].getOccurrence().get(0));
				
			}System.out.println();
			}
		}
		return objMatri;
		
	}
	
	public int hitCalculator(Date row, Date column,HashSet<ArrayList<String>> yearOccu ){
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
