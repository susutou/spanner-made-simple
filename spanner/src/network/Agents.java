package network;

import java.util.Hashtable;
import java.util.Map;

public class Agents {

	public static int port = 7766;
	public static int paxosPort = 6666;
	
	public static String ORX = "ec2-54-244-154-181.us-west-2.compute.amazonaws.com";
	public static String ORY = "ec2-54-245-188-21.us-west-2.compute.amazonaws.com";
	
	public static String NVY = "ec2-50-16-32-171.compute-1.amazonaws.com";
	public static String NVX = "ec2-23-21-13-52.compute-1.amazonaws.com";
	
	public static String EUX = "ec2-54-228-101-94.eu-west-1.compute.amazonaws.com";
	public static String EUY = "ec2-54-228-106-12.eu-west-1.compute.amazonaws.com";
	
	
	public static String coordinator = NVY;
	
	public static String[] paxos_leaders = new String[]{NVX,NVY};
	public static String[] tpc_corhort = new String[]{ORX,ORY};
	
	//the mapping of names and ips
	private static Map<String,String> simpleDNS = new Hashtable<String,String>(); 
	//private static Map<String,String> serverRole = new Hashtable<String,String>();
	
	public static String getIP(String serverName){
		
		return simpleDNS.get(serverName);
	}
	
	public static int get2PCCohortSize(){
		return tpc_corhort.length;
	}
	
	public Agents(){
		//initialize
		simpleDNS.put("ORX",ORX);
		simpleDNS.put("ORY",ORY);
		simpleDNS.put("NVX",NVX);
		simpleDNS.put("NVY",NVY);
		simpleDNS.put("EUX",EUX);
		simpleDNS.put("EUY",EUY);
	}
	
}
