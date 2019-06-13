package client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorFrame extends JFrame {
	private static final long serialVersionUID = -4227979286113973455L;	
	private JColorChooser colorPicker;
	private Color curColor=Color.BLACK;
	
	
	public ColorFrame(CanvasPanel canvas){
	super();
    colorPicker = new JColorChooser();
    add(colorPicker, BorderLayout.PAGE_END);  
    colorPicker.getSelectionModel().addChangeListener(new ChangeListener(){
	   public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			curColor=colorPicker.getColor();
			canvas.setColor(curColor);
		}
	   
   });
    
	}
	
	public ColorFrame(TextPanel tp){
		super();
	    colorPicker = new JColorChooser();
	    add(colorPicker, BorderLayout.PAGE_END);  
	    colorPicker.getSelectionModel().addChangeListener(new ChangeListener(){
		   public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				curColor=colorPicker.getColor();
				tp.setColor(curColor);
			}
		   
	   });
	}
	
	
	public Color getColor(){
		return curColor;
	}
	
	
}
