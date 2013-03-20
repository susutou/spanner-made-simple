package test;

public class test {

	public static void main(String[] args){
		
		everLoop lp = new everLoop();
		
		lp.start();
		
		lp.printloop();
		int i=0;
		while(i<100000){
			i++;
			lp.setI(i);
			
		}
		
		
		
	}
	
	
}
