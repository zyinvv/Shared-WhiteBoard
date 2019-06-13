package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import client.CanvasShape;
import remote.IRemoteShape;
import remote.IShapeList;

//this is the implement of IShapeList Interface
public class ShapeListImp extends UnicastRemoteObject implements IShapeList {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4796375985280696021L;
	private ArrayList<CanvasShape> shapeList=new ArrayList<CanvasShape>();

	public ShapeListImp() throws RemoteException{
		
	}

	public ArrayList<CanvasShape> getAllShapes() {
		// TODO Auto-generated method stub
		return shapeList;
	}

	public void addOneShape(CanvasShape newShape) {
		// TODO Auto-generated method stub
		shapeList.add(newShape);
		System.out.println("add a new shape");
	}

	public void deleteShape(CanvasShape shape) {
		// TODO Auto-generated method stub
		shapeList.remove(shape);
	}

}
