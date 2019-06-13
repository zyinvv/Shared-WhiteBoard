package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.CanvasShape;


public interface IShapeList extends Remote {
public ArrayList<CanvasShape> getAllShapes() throws RemoteException;
public void addOneShape(CanvasShape newShape) throws RemoteException;
public void deleteShape(CanvasShape shape) throws RemoteException;
}
