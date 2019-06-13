package client;

import java.awt.Shape;
import java.io.Serializable;

public class CanvasShape implements Serializable {
/**
	 * 
	 */
private static final long serialVersionUID = 7361298907620028426L;
public static enum ShapeType{NONE,LINE,OVAL,RECTANGLE,TEXT,FREEHAND}
private Shape shape;
//private Font textFont;
private String fontName;
private int fontStyle;
private int fontSize;
private int borderColor;// the RGB of border
private int fillColor;//the RGB of fill in color
private int borderSize;
private String text;
private int x;
private int y;//the coordinator of top left dot of the shape

private ShapeType type;
public CanvasShape(){
	setText("");
	setX(0);
	setY(0);
	setType(ShapeType.NONE);
}
public CanvasShape(Shape oriShape,int oriBColor,int oriFColor,int oriBSize){
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
public int getFillColor() {
	return fillColor;
}
public void setFillColor(int fillColor) {
	this.fillColor = fillColor;
}
public Shape getShape() {
	return shape;
}
public void setShape(Shape shape) {
	this.shape = shape;
}
public int getBorderColor() {
	return borderColor;
}
public void setBorderColor(int borderColor) {
	this.borderColor = borderColor;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text=text;
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

public String getFontName() {
	return fontName;
}
public void setFontName(String fontName) {
	this.fontName = fontName;
}
public int getFontStyle() {
	return fontStyle;
}
public void setFontStyle(int fontStyle) {
	this.fontStyle = fontStyle;
}
public int getFontSize() {
	return fontSize;
}
public void setFontSize(int fontSize) {
	this.fontSize = fontSize;
}




}
