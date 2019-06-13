/**
 * 
 */
package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import flexjson.JSONSerializer;

/**
 * @author zsl
 *
 */
public class CanvasPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -538338465800460507L;

	private CopyOnWriteArrayList<CanvasShape> shapes = new CopyOnWriteArrayList<CanvasShape>();
	private String user_op = "line";
	private Point startDrag, endDrag;
	private Color curColor = Color.BLACK;
	private Color curFontColor = Color.BLACK;
	private Font curFont;
	private int strokeSize = 1;
	private int eraserSize = 1;
	private int canvasWidth = 600;
	private int canvasHeight = 400;
	private JFrame textFrame;
	private ClientFrame parentFrm;


	public CanvasPanel(ClientFrame frmClient) {
		this.parentFrm = frmClient;
		textFrame = new JFrame();
		textFrame.setTitle("add a text");
		textFrame.setBounds(100, 100, 600, 400);
		textFrame.setResizable(false);
		textFrame.setVisible(false);
		TextPanel tp = new TextPanel();
		textFrame.add(tp);
		tp.setParentFrame(textFrame);
		tp.setParentCanvas(this);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				CanvasShape nShape = new CanvasShape();
				if (!user_op.equals("text")) {

					nShape.setShape(getShape(user_op, e));
					if (user_op.equals("eraser")) {
						nShape.setBorderColor(Color.WHITE.getRGB());
						nShape.setBorderSize(eraserSize);
					} else {
						nShape.setBorderColor(curColor.getRGB());
						nShape.setBorderSize(strokeSize);
					}
					shapes.add(nShape);
					sendNewShape(nShape);
				}				
				 else {
					textFrame.setVisible(true);
					tp.setTextPos(endDrag);
				}

				startDrag = null;
				endDrag = null;
				repaint();
				tp.setUserStr("");
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				endDrag = new Point(e.getX(), e.getY());
				repaint();
			}
		});

		this.setSize(canvasWidth, canvasHeight);
	}
	

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



		for (CanvasShape s : shapes) {
			g2.setPaint(new Color(s.getBorderColor()));
			g2.setStroke(new BasicStroke(s.getBorderSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			if (s.getType() == CanvasShape.ShapeType.TEXT) {
				g2.setFont(new Font(s.getFontName(),s.getFontStyle(),s.getFontSize()));
				g2.drawString(s.getText(), s.getX(), s.getY());
			} 
			else {
				g2.draw(s.getShape());
			}

		}

		if (startDrag != null && endDrag != null) {
			if (user_op.equals("pen")) {
				g2.setPaint(curColor);
				g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				CanvasShape nShape = new CanvasShape();
				nShape.setShape(getShape(user_op));
				nShape.setBorderColor(curColor.getRGB());
				nShape.setBorderSize(strokeSize);
				startDrag.setLocation(endDrag);
				g2.draw(getShape(user_op));
				shapes.add(nShape);
				sendNewShape(nShape);
				
			} else if (user_op.equals("eraser")) {
				g2.setPaint(Color.WHITE);
				CanvasShape nShape = new CanvasShape();
				nShape.setShape(getShape(user_op));
				nShape.setBorderColor(Color.WHITE.getRGB());
				nShape.setBorderSize(eraserSize);
				shapes.add(nShape);
				sendNewShape(nShape);
				g2.draw(nShape.getShape());
				startDrag.setLocation(endDrag);
			} else {
				if (!user_op.equals("text")) {
					g2.setPaint(Color.LIGHT_GRAY);
					Shape nShape = getShape(user_op);
					g2.draw(nShape);
				}
			}

		}
	}

	public Shape getShape(String user_op, MouseEvent e) {
		Shape nShape = null;
		switch (user_op) {
		case "line":
			nShape = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		case "rectangle":
			nShape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		case "circle":
			nShape = makeCircle(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		case "oval":
			nShape = makeOval(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		case "pen":
			nShape = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		case "eraser":
			nShape = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
			break;
		}
		return nShape;
	}

	public Shape getShape(String user_op) {
		Shape nShape = null;
		switch (user_op) {
		case "line":
			nShape = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		case "rectangle":
			nShape = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		case "circle":
			nShape = makeCircle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		case "oval":
			nShape = makeOval(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		case "pen":
			nShape = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		case "eraser":
			nShape = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
			break;
		}
		return nShape;
	}

	private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
		return new Line2D.Float(x1, y1, x2, y2);
	}

	private Ellipse2D.Float makeOval(int x1, int y1, int x2, int y2) {
		return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private Ellipse2D.Float makeCircle(int x1, int y1, int x2, int y2) {
		return new Ellipse2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2)),
				Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2)));

	}

	public void setOperation(String op) {
		this.user_op = op;
	}

	public void setColor(Color newColor) {
		curColor = newColor;
	}

	public void setStrokeSize(int size) {
		strokeSize = size;
	}

	public void reset() {
		shapes.clear();
	}

	public CopyOnWriteArrayList<CanvasShape> getShapes() {
		return shapes;
	}

	public void setShapes(CopyOnWriteArrayList<CanvasShape> newShapes) {
		shapes.addAll(newShapes);
	}

	public void downloadShapes(CopyOnWriteArrayList<CanvasShape> newShapes) {
		shapes = newShapes;
		System.out.println(shapes.size());
	}

	public void addOneShape(CanvasShape newShape) {
		shapes.add(newShape);
	}

	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage((int) getSize().getWidth(), (int) getSize().getHeight(), 1);
		Graphics2D g2 = img.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
		for (CanvasShape s : shapes) {
			g2.setPaint(new Color(s.getBorderColor()));
			g2.setStroke(new BasicStroke(s.getBorderSize()));
			g2.draw(s.getShape());

		}
		g2.dispose();
		return img;
	}

	public int getEraserSize() {
		return eraserSize;
	}

	public void setEraserSize(int eraserSize) {
		this.eraserSize = eraserSize;
	}

	public Color getCurFontColor() {
		return curFontColor;
	}

	public void setCurFontColor(Color curFontColor) {
		this.curFontColor = curFontColor;
	}

	public Font getCurFont() {
		return curFont;
	}

	public void setCurFont(Font curFont) {
		this.curFont = curFont;
	}

	public Point getStartDrag() {
		return startDrag;
	}

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(int canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

	
	public void sendNewShape(CanvasShape newShape) {
		// if is server, send shape to all clients
		SocketMsg packet = new SocketMsg();
		packet.setOperation("add");
		JSONSerializer serializer = new JSONSerializer();
		String data = serializer.serialize(newShape);
		packet.setData(data);
		//System.out.println(serializer.serialize(packet));
		
		if (parentFrm.getServerThread() != null) {
			packet.setUserName(parentFrm.getUser().getUserName());
			CopyOnWriteArrayList<User> clients = parentFrm.getServerThread().getClients();
			if (!clients.isEmpty()) {
				for (User client : clients) {
					if(client.getUserName().equals(parentFrm.getUser().getUserName()))
						continue;
					try {
						DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
						;
						out.writeUTF(serializer.serialize(packet));
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		// if is client, send shape to server
		else if (parentFrm.getClientThread() != null) {
			packet.setUserName(parentFrm.getUser().getUserName());
			try{
			Socket socket=parentFrm.getClientThread().getClient();
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(serializer.serialize(packet));}
			catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void sendMsg(SocketMsg msg){
		if (parentFrm.getServerThread() != null) {
			CopyOnWriteArrayList<User> clients = parentFrm.getServerThread().getClients();
			if (!clients.isEmpty()) {
				for (User client : clients) {
					if(client.getUserName().equals(parentFrm.getUser().getUserName()))
						continue;
					try {
						DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
						JSONSerializer serializer = new JSONSerializer();
						out.writeUTF(serializer.serialize(msg));
						//System.out.println(serializer.serialize(msg));
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		
		// if is client, send msg to server
		else if (parentFrm.getClientThread() != null) {

		}
		
	}

	



}
