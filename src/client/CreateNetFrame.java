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

public class CreateNetFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 688947493347499894L;
	
	private String userName;
	private int serverPort;
	private JTextField tfUserName;
	private JTextField tfServerPort;
	private JPanel panel;
	private boolean isValid=false;
	private ClientFrame parentFrm;

	public CreateNetFrame(ClientFrame frmClient){
		parentFrm=frmClient;
		this.setSize(new Dimension(300,150));
		this.setResizable(false);
		panel=new JPanel();
		this.add(panel);
	
		panel.setLayout(new SpringLayout());
		JLabel lbUserName=new JLabel("user name");
		panel.add(lbUserName);
		tfUserName=new JTextField();
		panel.add(tfUserName);
		lbUserName.setLabelFor(tfUserName);
		JLabel lbPort=new JLabel("port number");
		panel.add(lbPort);
		tfServerPort=new JTextField();
		panel.add(tfServerPort);
		lbPort.setLabelFor(tfServerPort);
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
                3, 2, //rows, cols
                6, 6,        //initX, initY
                5, 5);       //xPad, yPad
		this.setVisible(false);
	}
	
public void	confirmBtnPressed(){
	if(tfUserName.getText().trim().equals("")){
		JOptionPane.showMessageDialog(this,"please input user name");
	}
	else if(tfServerPort.getText().trim().equals("")){
		JOptionPane.showMessageDialog(this,"please input Port");
	}
	else{
		
		try{
			serverPort=Integer.parseInt(tfServerPort.getText());
			userName=tfUserName.getText();
		}catch(NumberFormatException e){
			System.out.println(e.getMessage());
		}catch(NullPointerException e) {
			System.out.println(e.getMessage());
	    }
		this.setVisible(false);
		isValid=true;
		
		if(parentFrm.getUser()==null){
			parentFrm.setUser(new User(userName,serverPort,true));		
		}
		parentFrm.getUserListPanel().addUser(userName+"(manager)");
		new SocketThread(serverPort,parentFrm);

	}
}
public void cancelBtnPressed(){
	this.setVisible(false);
}

public int getServerPort() {
	return serverPort;
}

public void setServerPort(int serverPort) {
	this.serverPort = serverPort;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public boolean isValid() {
	return isValid;
}

public void setMaster(boolean isValid) {
	this.isValid = isValid;
}


}
