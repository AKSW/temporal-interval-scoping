package it.unimib.disco.Utilities;

import it.unimib.disco.Reasoning.Interval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class WriteMatrixOutput {
	
	private BufferedWriter bw;

	public void printMatrix (HashMap<String,HashMap<String,List<Interval>>> sub_obj_interval){

		try {
			File directory = new File (".");

			bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/matrix/"+"matrix.csv")));
			for (String uri:sub_obj_interval.keySet()){
				for (String obj:sub_obj_interval.get(uri).keySet()){
					bw.write(uri+"	"+obj+"\n" );
					bw.write("[Start]/End");
					
					boolean meeting = false;
					HashSet<String> hs = new HashSet<String>();
					
					int j=0; 
					do {
						bw.write("\t" );
						Interval interval = sub_obj_interval.get(uri).get(obj).get(j);
						
						if (hs.contains(interval.getEnd())){
							meeting = true;
						}
						else{ 
							hs.add(interval.getEnd());
							bw.write(interval.getEnd());
						}
						
						j++;
					}while (j< sub_obj_interval.get(uri).get(obj).size() && !meeting);
					
					String intialStartYear = "hi"; 
					int position = 0;
					for (int i=0; i< sub_obj_interval.get(uri).get(obj).size(); i++ ){
						//bw.write("\t" );	
						Interval interval = sub_obj_interval.get(uri).get(obj).get(i);
						
						if (interval.getStart().equalsIgnoreCase(intialStartYear)){
							bw.write("\t"+interval.getValue());
						}
						else{
							bw.write("\n");
							bw.write(interval.getStart()); 
							for (int k=0; k<position;k++){
								bw.write("\t"+"0"); 
							}
							position++;
							bw.write("\t"+interval.getValue());	
							intialStartYear = interval.getStart();
						}
						
						
					}
					bw.write("\n" );
				}
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

}
