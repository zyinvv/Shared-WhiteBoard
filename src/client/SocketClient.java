package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import flexjson.JSONDeserializer;

public class SocketClient extends Thread {
private String serverIP;
private int serverPort;
private String userName;
private Socket client; //the socket is connected to a server
private DataInputStream in;
private DataOutputStream out;
private ClientFrame rootFrm;
public SocketClient(){}

public SocketClient(String serverIP,int serverPort,String userName,ClientFrame frmClient){
	this.setServerIP(serverIP);
	this.setServerPort(serverPort);
	this.setUserName(userName);
	this.rootFrm=frmClient;
	try{
	client=new Socket(serverIP, serverPort);
	in=new DataInputStream(client.getInputStream());
	out=new DataOutputStream(client.getOutputStream());
	System.out.println("connect to server");
	rootFrm.setClientThread(this);
	rootFrm.setUser(new User(userName,client,true));
	out.writeUTF(userName);
	rootFrm.getCanvasPanel().reset();//reset canvas
	rootFrm.getMenuBar().getMenu(0).setEnabled(false);
	this.start();
	}catch(IOException e){
		System.out.println(e.getMessage());
		JOptionPane.showMessageDialog(rootFrm.getFrmClient(),e.getMessage());
	}
	
}

public void run(){
	while(true){
		try{
		String data=in.readUTF();
		JSONDeserializer<SocketMsg> parser=new JSONDeserializer<SocketMsg>();
		SocketMsg msg=parser.deserialize(data);
		
		//command add means add a shape to canvas
		if(msg.getOperation().equals("add")){
			
			CanvasShape newShape=new JSONDeserializer<CanvasShape>().deserialize(msg.getData() );
			rootFrm.getCanvasPanel().addOneShape(newShape);
			rootFrm.getCanvasPanel().repaint();
		}
		//chat mean the packet is a chating message
		else if(msg.getOperation().equals("chat")){
			rootFrm.getTaMsgHis().append(msg.getUserName()+": "+msg.getData()+"\n");
		}
		
		// add a user to userListPanel if the user does not exist
		else if(msg.getOperation().equals("addUser")){
			rootFrm.getUserListPanel().addUser(msg.getData());
		}
		//delete a user from userListPanel if the user exists
		else if(msg.getOperation().equals("deleteUser")){
			rootFrm.getUserListPanel().deleteUser(msg.getData());
		}
		// system error message
		else if(msg.getOperation().equals("error")){
			JOptionPane.showMessageDialog(rootFrm.getFrmClient(),msg.getData());
		}
		// system kick out message
		else if(msg.getOperation().equals("kick")){
			JOptionPane.showMessageDialog(rootFrm.getFrmClient(),msg.getData());
			rootFrm.getUserListPanel().clear();
			rootFrm.getMenuBar().getMenu(0).setEnabled(true);
			discFromServer();
		}
		// the server disconnect from network
		else if(msg.getOperation().equals("disconnect")){
			JOptionPane.showMessageDialog(rootFrm.getFrmClient(),msg.getData());
			rootFrm.getUserListPanel().clear();
			rootFrm.getMenuBar().getMenu(0).setEnabled(true);
			discFromServer();
		}
		else if(msg.getOperation().equals("newCanvas")){
			rootFrm.getCanvasPanel().reset();
			rootFrm.getCanvasPanel().repaint();
		}
		
		
		}catch(IOException e){
			System.out.println(e.getMessage());
			if(e.getMessage()!=null)
			JOptionPane.showMessageDialog(rootFrm.getFrmClient(),e.getMessage());
			if(client!=null){
				try{
				client.close();
				rootFrm.getMenuBar().getMenu(0).setEnabled(true);
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
				}
			break;
		}
	}
}

public void discFromServer(){
	if(client!=null){
		try{
		client.close();
		rootFrm.getMenuBar().getMenu(0).setEnabled(true);
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
	}
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getServerIP() {
	return serverIP;
}

public void setServerIP(String serverIP) {
	this.serverIP = serverIP;
}

public int getServerPort() {
	return serverPort;
}

public void setServerPort(int serverPort) {
	this.serverPort = serverPort;
}

public Socket getClient() {
	return client;
}

public void setClient(Socket client) {
	this.client = client;
}

}
