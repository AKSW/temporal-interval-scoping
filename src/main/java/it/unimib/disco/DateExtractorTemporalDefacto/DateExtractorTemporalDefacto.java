package it.unimib.disco.DateExtractorTemporalDefacto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Download API doc pages
 * 
 * @author arula
 *
 */
public class DateExtractorTemporalDefacto{
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Use: java DateExtractorTemporalDefacto <Resource temporaldefacto list of facts>");
		} else {
			List<String> testData = getURIs(new File(args[0]));
			
			String sub,slabel,pred,obj,olabel,start,end,strSup1,strSup2,strSup3;
			
			FileOutputStream fos = new FileOutputStream("sortbyplayer-labels-with-space_out.csv");
			PrintWriter pw = new PrintWriter(fos);
				
			for(String str:testData){
				sub=str.substring(0, str.indexOf("	"));
				sub=sub.replace("/", "%2F");
				sub=sub.replace(":", "%3A");
				strSup1 = str.substring(str.indexOf("	")+1);
				
				
				slabel = strSup1.substring(0, strSup1.indexOf("	"));
				if (slabel.contains(" ")){
					slabel=slabel.replace(" ", "%20");
				}
				
				strSup2 = strSup1.substring(strSup1.indexOf("	")+1);
				
				pred=strSup2.substring(0,strSup2.indexOf("	"));
				pred=pred.replace("/", "%2F");
				pred=pred.replace(":", "%3A");
				 
				strSup3=strSup2.substring(strSup2.indexOf("	")+1);
				
				obj=strSup3.substring(0,strSup3.indexOf("	"));
				obj=obj.replace("/", "%2F");
				obj=obj.replace(":", "%3A");
				strSup3=strSup3.substring(strSup3.indexOf("	")+1);
				
				olabel= strSup3.substring(0,strSup3.indexOf("	"));
				if (olabel.contains(" ")){
					olabel=olabel.replace(" ", "%20");
				}
				
				strSup3= strSup3.substring(strSup3.indexOf("	")+1);
				
				start = strSup3.substring(0,strSup3.indexOf("	"));
				end = strSup3.substring(strSup3.indexOf("	")+1);
			  
			try {

				String strLine ="http://139.18.2.164:1234/getdefactotimes?s="+sub+"&p="+pred+"&o="+obj+"&slabel="+slabel+"&olabel="+olabel;
				System.out.println(strLine);
				URL u = new URL(strLine);

				URLExtractor w = new URLExtractor(u, pw, start, end);
				w.run();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		      pw.close();
		      fos.close();
			
		}
	}
	
	protected static List<String> getURIs(File rsListFile) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(rsListFile));
			
			String line = br.readLine();
			
			while(line!=null){
				if(line.charAt(0)!='#'){
					list.add(line);
				}
				line=br.readLine();
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;

	}
}
