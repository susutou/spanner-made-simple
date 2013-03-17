package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import network.Requests;

import util.Utilities;

public class Client extends Thread{
	
	//Server side program
	public void run(){
		
		try{
			ServerSocket sSocket = new ServerSocket(7777);
			
			while(Utilities.checkFlag()){
				Socket socket = sSocket.accept();
				DataInputStream input = new DataInputStream(socket.getInputStream());
				String msg = input.readUTF();
				System.out.println("<<<--Get MSG---<<<: "+msg);
				socket.close();
			}
			
			sSocket.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Client client = new Client();
		client.start();
		
    	//Run client to send request, format: peer c ip port order
    	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    	try {
    		
    	while(true){
    		System.out.println("Give me some test message");
    		
    		String msg = stdIn.readLine().trim();

			Requests.sendRequestTo("localhost", 7777, msg);
    	}
    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}
	
	
	
}
