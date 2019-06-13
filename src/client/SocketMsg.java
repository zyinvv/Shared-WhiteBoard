package client;

public class SocketMsg {
String operation="";
String userName="";
String data="";
public SocketMsg(){
	
}
public SocketMsg(String op,String data){
	this.operation=op;
	this.data=data;
}

public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}

public String getOperation() {
	return operation;
}
public void setOperation(String operation) {
	this.operation = operation;
}

public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
}
