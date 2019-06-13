package remote;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.rmi.Remote;
import java.util.ArrayList;




// the interface for RemoteShape
public interface IRemoteShape extends Remote {
	public static enum ShapeType{NONE,LINE,OVAL,RECTANGLE,TEXT,FREEHAND}
	public int getBorderSize();
	public void setBorderSize(int borderSize);
	public Color getFillColor();
	public void setFillColor(Color fillColor);
	public Shape getShape();
	public void setShape(Shape shape);
	public Color getBorderColor();
	public void setBorderColor(Color borderColor);
	public String getText();
	public void setText(String text);
	public int getX();
	public void setX(int x);
	public int getY();
	public void setY(int y);
	public ShapeType getType();
	public void setType(ShapeType type);
	public Font getTextFont();
	public void setTextFont(Font textFont);
	public ArrayList<Line2D> getFreeHandTrack();
	public void setFreeHandTrack(ArrayList<Line2D> freeHandTrack);
}
