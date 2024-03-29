package client;

import java.net.Socket;

public class User {
private String userName;
private String serverIP="localhost";
private int host=23333;
private boolean isServer=false;
private boolean isClient=false;
private Socket clientSocket;

public User(){
	
}
public User(String userName,Socket clientSocket,boolean isClient){
	this.userName=userName;
	this.clientSocket=clientSocket;
	this.isClient=isClient;
}

public User(String userName,int host,boolean isServer){
	this.userName=userName;
	this.host=host;
	this.isServer=isServer;
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
public int getHost() {
	return host;
}
public void setHost(int host) {
	this.host = host;
}
public boolean isServer() {
	return isServer;
}
public void setServer(boolean isServer) {
	this.isServer = isServer;
}
public Socket getClientSocket() {
	return clientSocket;
}
public void setClientSocket(Socket clientSocket) {
	this.clientSocket = clientSocket;
}
public boolean isClient() {
	return isClient;
}
public void setClient(boolean isClient) {
	this.isClient = isClient;
}



}
