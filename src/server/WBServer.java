package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class WBServer {
public static void main(String [] args){
	try{
	ShapeListImp canvasShapes=new ShapeListImp();
	Registry registry = LocateRegistry.getRegistry();
	System.setProperty("java.rmi.server.hostname","192.168.0.2");
    registry.bind("canvasShapes", canvasShapes); 
    System.out.println("canvas server ready");}
	catch(RemoteException|AlreadyBoundException e){
		System.out.println(e.getMessage());
	}
}
}
