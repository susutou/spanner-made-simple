package network;

public class MessageHelper {

	public static String parseMsg(String msg){
		return "";
	}
	
	public static String[] parseInputCommand(String command){
		//should have only two elements
		String[] params = command.split("@");
		
		return params;
	}
	
	public static String getOpIDFromOperation(String operation){
		//operation = operations#txnID#opID
		String[] params = operation.split("#");
		return params[2];
	}
	
	public static String getOpIDFromHeaderMsg(String msg){
		//msg = header#operations#txnID#opID
		String[] params = msg.split("#");
		return params[3];
	}
	
	public static String getTxnIDFrom2PCPrepare(String msg){
		//msg = 2pc_prepare#operations#txnID#opID
		String[] params = msg.split("#");
		return params[2];
	}
	
	public static String getTxnIDFromPaxosReady(String msg){
		//msg = paxos_ready#operations#txnId#opID
		String[] params = msg.split("#");
		return params[2];
	}
	public static String removeHeaderFromMsg(String msg){
		//msg = header#operations#txnId#opID
		String[] params = msg.split("#", 2);
		return params[1];
	}
	public static String getOpsFromOperation(String operation){
		//operation = operations#txnId#opID
				String[] params = operation.split("#");
				return params[0];
	}
	
	
	
}
