package log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class logger {

	
	public static void log(String entry){
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("2PC_LOG.txt",true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(dateFormat.format(cal.getTime())+"    "+entry);
			  
			  //Close the output stream
			  out.close();
			  fstream.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}
	
	
}
