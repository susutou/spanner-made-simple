package test;

public class test {

	public static void main(String[] args){
		
		int i = 0;
		
		System.out.println("ttest"+i++);
		System.out.println("ttest"+i++);
		System.out.println("ttest"+i++);
		
		String[] msg = "header#ops#id2#id3".split("#", 2);
		System.out.println(msg[0]);
		System.out.println(msg[1]);
	}
	
	
}
