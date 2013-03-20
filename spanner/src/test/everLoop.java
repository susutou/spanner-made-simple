package test;

public class everLoop extends Thread {

	private int i=0;
	private int j=0;
	
	public void run(){
		
		while(i<100000){
			j++;
			if(j%1000 ==0) System.out.println(j);
		}
		System.out.println(i);
		
	}
	
	public void printloop(){
		System.out.println("$$$$$$$$$$$$$$$$");
	}
	
	public void setI(int i){
		this.i = i;
	}
	
	
}
