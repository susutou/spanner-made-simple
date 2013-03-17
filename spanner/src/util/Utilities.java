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
			if (line.equals("start"))
				flag = true;
			if (line.equals("stop"))
				flag = false;
			fs.close();
			in.close();
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
