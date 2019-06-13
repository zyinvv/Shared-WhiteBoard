package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ConnectNetFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1371038391872462867L;
	private String  userName;
	private String serverIP;
	private int serverPort;
	private JTextField tfUserName;
	private JTextField tfServerIP;
	private JTextField tfServerPort;
	private JPanel panel;
	private boolean isValid=false;
	private ClientFrame frmClient;


	public ConnectNetFrame(ClientFrame parentFrm){
		this.frmClient=parentFrm;
		this.setSize(new Dimension(300,200));
		this.setResizable(false);
		panel=new JPanel();
		this.add(panel);
		
		panel.setLayout(new SpringLayout());
		JLabel lbUserName=new JLabel("user name:");
		panel.add(lbUserName);
		tfUserName=new JTextField();
		panel.add(tfUserName);
		lbUserName.setLabelFor(tfUserName);
		JLabel lbIP=new JLabel("server IP:");
		panel.add(lbIP);
		tfServerIP=new JTextField();
		panel.add(tfServerIP);
		
		JLabel lbHost=new JLabel("port number:");
		panel.add(lbHost);
		tfServerPort=new JTextField();
		panel.add(tfServerPort);
		lbHost.setLabelFor(tfServerPort);
		JButton confirmBtn =new JButton("ok");
		confirmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmBtnPressed();
			}
		});
		panel.add(confirmBtn);
		JButton cancelBtn=new JButton("cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelBtnPressed();
			}
		});
		panel.add(cancelBtn);
		
		SpringUtilities.makeCompactGrid(panel,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                5, 5);       //xPad, yPad
		this.setVisible(false);
	}
	
	
	public void	confirmBtnPressed(){
		if(tfUserName.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,"please input user name");
		}
		else if(tfServerPort.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,"please input host");
		}
		else if(tfServerIP.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,"please input server IP");
		}
		else{
			
			try{
				setServerPort(Integer.parseInt(tfServerPort.getText()));
				setServerIP(tfServerIP.getText());
				setUserName(tfUserName.getText());
			}catch(NumberFormatException e){
				System.out.println(e.getMessage());
			}catch(NullPointerException e) {
				System.out.println(e.getMessage());
		    }
			
			isValid=true;
			this.setVisible(false);
			new SocketClient(serverIP,serverPort,userName,frmClient);
		}
	}
	public void cancelBtnPressed(){
		this.setVisible(false);
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getServerIP() {
		return serverIP;
	}


	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}


	public int getServerPort() {
		return serverPort;
	}


	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public boolean isValid() {
		return isValid;
	}


	public void setClient(boolean isValid) {
		this.isValid = isValid;
	}

	
}
