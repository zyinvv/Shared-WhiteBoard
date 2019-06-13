package client;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChangeListener implements ChangeListener {
	
	private CanvasPanel canvasPanel;
	private ColorFrame colorFrame;
	
	public ColorChangeListener(CanvasPanel canvas,ColorFrame cf){
		canvasPanel=canvas;
		colorFrame=cf;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
		canvasPanel.setColor(colorFrame.getColor());
	}

}
