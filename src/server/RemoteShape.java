package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


import remote.IRemoteShape;

public class RemoteShape extends UnicastRemoteObject implements IRemoteShape {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -505079884375238986L;
	private Shape shape;
	private Color borderColor;
	private Color fillColor;
	private int borderSize;
	private String text;
	private int x;
	private int y;//the coordinator of top left dot of the shape
	private Font textFont;
	private ShapeType type;
	private ArrayList<Line2D> freeHandTrack;
	
	public RemoteShape() throws RemoteException {
		setText("");
		setX(0);
		setY(0);
		setType(ShapeType.NONE);
	}
	
	public RemoteShape(Shape oriShape,Color oriBColor,Color oriFColor,int oriBSize) throws RemoteException{
		this();
		this.setShape(oriShape);
		this.setBorderColor(oriBColor);
		this.setFillColor(oriFColor);
		this.setBorderSize(oriBSize);
	}
	public int getBorderSize() {
		return borderSize;
	}
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	public Shape getShape() {
		return shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public Color getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public ShapeType getType() {
		return type;
	}
	public void setType(ShapeType type) {
		this.type = type;
	}
	public Font getTextFont() {
		return textFont;
	}
	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}
	public ArrayList<Line2D> getFreeHandTrack() {
		return freeHandTrack;
	}
	public void setFreeHandTrack(ArrayList<Line2D> freeHandTrack) {
		this.freeHandTrack = freeHandTrack;
	}


}
