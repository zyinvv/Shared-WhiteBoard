package client;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class TextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -427900908251503540L;
	
	private JLabel lblFont;
	private JComboBox<String> jcbFont;
	private JLabel lblFontStyle;
	private JComboBox<String> jcbFontStyle;
	private JLabel lblFontSize;
	private JSlider sldFontSize;
	private JTextField userTF;
	private JButton btnCurColor;
	private JButton btnChangeColor;
	private JButton btnConfirm;
	private Color curColor=Color.BLACK;
	private Font [] sysFonts;
	private String [] fontStyle={"Plain","Bold","Italic"};
	private String curFontName;
	private int curStyle=Font.PLAIN;
	private String userStr="";
	private int curSize=12;
	private Font curFont;
	private ColorFrame colorFrame;
	private JFrame parentFrame;
	private CanvasPanel parentCanvas;
	private Point textPos;
	public TextPanel(){
		init();
	}
	public void init(){
		this.setSize(100, 400);
		GridBagLayout gridLayout=new GridBagLayout();
		gridLayout.columnWeights=new double[]{1,1};
		gridLayout.rowWeights=new double[]{1,1,1,1,1,1};
		this.setLayout(gridLayout);
		GridBagConstraints lblFontCon = new GridBagConstraints();
		lblFontCon.gridx=0;
		lblFontCon.gridy=0;
		lblFont=new JLabel("Font");
		this.add(lblFont, lblFontCon);
		GridBagConstraints lblFontSizeCon = new GridBagConstraints();
		lblFontSizeCon.gridx=0;
		lblFontSizeCon.gridy=1;
		lblFontSize=new JLabel("Font Size");
		this.add(lblFontSize, lblFontSizeCon);
		GridBagConstraints lblFontStyleCon = new GridBagConstraints();
		lblFontStyleCon.gridx=0;
		lblFontStyleCon.gridy=2;
		lblFontStyle=new JLabel("Font Style");
		this.add(lblFontStyle, lblFontStyleCon);
		
		GridBagConstraints jcbFontCon = new GridBagConstraints();
		jcbFontCon.gridx=1;
		jcbFontCon.gridy=0;
		jcbFontCon.anchor=GridBagConstraints.WEST;
		jcbFont=new JComboBox<String>();
		sysFonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		for (int i = 0; i < sysFonts.length; i++)
	    {
	      String fname = sysFonts[i].getName();
	      
	      jcbFont.addItem(fname);
	      if (fname.equals("Arial"))
	      {
	        jcbFont.setSelectedIndex(i);
	        curFontName ="Arial";
	        
	      }
	    }
		jcbFont.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				fontChanged(e);
			}
			
		});
		this.add(jcbFont,jcbFontCon);
		GridBagConstraints sldFontSizeCon = new GridBagConstraints();
		sldFontSizeCon.gridx=1;
		sldFontSizeCon.gridy=1;
		sldFontSizeCon.anchor=GridBagConstraints.WEST;
		sldFontSize=new JSlider();
	
	    sldFontSize.setMajorTickSpacing(5);
	    sldFontSize.setMaximum(50);
	    sldFontSize.setMinorTickSpacing(1);
	    sldFontSize.setPaintLabels(true);
	    sldFontSize.setPaintTicks(true);
	    sldFontSize.setSnapToTicks(true);
	    sldFontSize.setValue(12);
	    
	    sldFontSize.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				fontSizeChanged(e);
			}
	    	
	    });
	    this.add(sldFontSize,sldFontSizeCon);
		
		
		GridBagConstraints jcbFontStyleCon = new GridBagConstraints();
		jcbFontStyleCon.gridx=1;
		jcbFontStyleCon.gridy=2;
		jcbFontStyleCon.anchor=GridBagConstraints.WEST;
		jcbFontStyle=new JComboBox<String>();
		for (int i = 0; i < fontStyle.length; i++)
	    {          
	      jcbFontStyle.addItem(fontStyle[i]);
	    }
		jcbFontStyle.setSelectedIndex(0);
		jcbFontStyle.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				fontStyleChanged(e);
			}
			
		});
		this.add(jcbFontStyle,jcbFontStyleCon);
		
		GridBagConstraints btnCurColorCon = new GridBagConstraints();
		btnCurColorCon.gridx=0;
		btnCurColorCon.gridy=3;
		btnCurColor=new JButton();
		btnCurColor.setBackground(curColor);
		btnCurColor.setMaximumSize(new Dimension(25,25));
		btnCurColor.setMinimumSize(new Dimension(25,25));
		btnCurColor.setPreferredSize(new Dimension(25,25));
		this.add(btnCurColor,btnCurColorCon);
		
		GridBagConstraints btnChangeColorCon = new GridBagConstraints();
		btnChangeColorCon.gridx=1;
		btnChangeColorCon.gridy=3;
		btnChangeColorCon.anchor=GridBagConstraints.WEST;
		btnChangeColor=new JButton("change color");
		btnChangeColor.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				changeColorClicked(e);
			}	
		});
		this.add(btnChangeColor,btnChangeColorCon);
		
		GridBagConstraints userTFCon = new GridBagConstraints();
		userTFCon.gridx=0;
		userTFCon.gridy=4;
		userTF=new JTextField();
		userTF.setPreferredSize(new Dimension(150,30));
		this.add(userTF,userTFCon);
		
		GridBagConstraints btnConfirmCon = new GridBagConstraints();
		btnConfirmCon.gridx=1;
		btnConfirmCon.gridy=4;
		btnConfirmCon.anchor=GridBagConstraints.WEST;
		btnConfirm=new JButton("confirm");
		btnConfirm.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				btnConfirmClicked(e);
			}	
		});
		this.add(btnConfirm,btnConfirmCon);		
		colorFrame=new ColorFrame(this);
		colorFrame.setTitle("color picker");
		colorFrame.setBounds(100, 100, 600,400);
		colorFrame.setVisible(false);
		colorFrame.setResizable(false);
		curFont=new Font("Arial",Font.PLAIN,12);
	}
	
	public void changeColorClicked(MouseEvent e){
		colorFrame.setVisible(true);
		repaint();
	}
	
	public void btnConfirmClicked(MouseEvent e){
		if(userTF.getText().trim().length()!=0){
			userStr=userTF.getText().trim();
			CanvasShape nShape=new CanvasShape();
			nShape.setText(userTF.getText());
			nShape.setType(CanvasShape.ShapeType.TEXT);
			nShape.setX(textPos.x);
			nShape.setY(textPos.y);
			nShape.setBorderColor(curColor.getRGB());
			nShape.setFontName(curFont.getFontName());
			nShape.setFontStyle(curStyle);
			nShape.setFontSize(curSize);
			parentCanvas.addOneShape(nShape);
			parentCanvas.sendNewShape(nShape);
		}
		parentFrame.setVisible(false);
		parentCanvas.repaint();
	}
	
	public void fontChanged(ItemEvent e){
		if(e.getStateChange() == ItemEvent.SELECTED){
			curFontName=(String)jcbFont.getSelectedItem();
			curFont=new Font(curFontName,Font.PLAIN,curSize);
			parentCanvas.setCurFont(curFont);
		}
	}
	
	public void fontStyleChanged(ItemEvent e){
		if(e.getStateChange() == ItemEvent.SELECTED){
			curStyle=jcbFontStyle.getSelectedIndex();
			curFont=new Font(curFontName,curStyle,curSize);
			parentCanvas.setCurFont(curFont);
		}
	}
	
	public void fontSizeChanged(ChangeEvent e){
		curSize=sldFontSize.getValue();
		curFont=new Font(curFontName,curStyle,curSize);
		parentCanvas.setCurFont(curFont);
	}
	
	public void setColor(Color newColor){
		this.curColor=newColor;
		btnCurColor.setBackground(curColor);
		parentCanvas.setCurFontColor(curColor);
		
	}
	public String getUserStr() {
		return userStr;
	}
	public void setUserStr(String userStr) {
		this.userStr = userStr;
		userTF.setText(userStr);
	}
	public JFrame getParentFrame() {
		return parentFrame;
	}
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
	public CanvasPanel getParentCanvas() {
		return parentCanvas;
	}
	public void setParentCanvas(CanvasPanel parentCanvas) {
		this.parentCanvas = parentCanvas;
	}
	public Font getCurFont() {
		return curFont;
	}
	public void setCurFont(Font curFont) {
		this.curFont = curFont;
	}
	public Point getTextPos() {
		return textPos;
	}
	public void setTextPos(Point textPos) {
		this.textPos = textPos;
	}
}
