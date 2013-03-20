package util;

import java.io.BufferedReader;
import java.io.FileReader;

public class Utilities {

	public static boolean checkFlag() {
		boolean flag = false;
		try {
			FileReader fs = new FileReader("flag.txt");
			BufferedReader in = new BufferedReader(fs);
			String line = in.readLine().trim();
			if ("start".equals(line))
				flag = true;
			if ("stop".equals(line))
				flag = false;
			in.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static String getServerName(){
		
		String server ="";
		try {
			FileReader fs = new FileReader("server_name.txt");
			BufferedReader in = new BufferedReader(fs);
			server = in.readLine().trim();
			in.close();
			fs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return server;
	}
	
	
}
