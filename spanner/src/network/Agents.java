package network;

import java.util.Hashtable;
import java.util.Map;

public class Agents {

	public static int port = 7777;
	public static String coordinator = "1212.121.12";
	
	public static String[] paxos_leaders = new String[]{"",""};
	public static String[] tpc_corhort = new String[]{"",""};
	
	//the mapping of names and ips
	private static Map<String,String> simpleDNS = new Hashtable<String,String>(); 
	private static Map<String,String> serverRole = new Hashtable<String,String>();
	
	public static String getIP(String serverName){
		
		return "";
	}
	
	public Agents(){
		//initialize
		
		
		
		
	}

	
	
}
