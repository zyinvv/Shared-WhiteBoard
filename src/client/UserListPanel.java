package client;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import flexjson.JSONSerializer;

public class UserListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -154678996694424775L;
	
	private JList<String> userList;
	private ArrayList<String> userArray=new ArrayList<String>();
	private DefaultListModel<String> listModel;
	private ClientFrame parentFrm;
	public UserListPanel(ClientFrame Frm){
		parentFrm=Frm;
		userList=new JList<String>();
		JScrollPane scroll=new JScrollPane();
		listModel=new DefaultListModel<String>();
		for(String str:userArray){
			listModel.addElement(str);
		}
		userList.setModel(listModel);
		userList.setVisibleRowCount(5);
		userList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent mevt){
			       JList<String> list = (JList<String>)mevt.getSource();
			        if (mevt.getClickCount() == 2) {

			            // Double-click detected
			        	
			            int index = list.locationToIndex(mevt.getPoint());
			            if(index>0){
			            if(parentFrm.getServerThread()!=null){	
			            Object[] options = { "Yes", "No" };
						int decision = JOptionPane.showOptionDialog(parentFrm.getFrmClient(),
								 "Do you want to kick out "+ userArray.get(index), "Tips", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(decision==JOptionPane.OK_OPTION){
							CopyOnWriteArrayList<User> clients=parentFrm.getServerThread().getClients();
							for(User client:clients){
								if(userArray.get(index).equals(client.getUserName())){
									JSONSerializer serializer = new JSONSerializer();
									try{
									DataOutputStream out=new DataOutputStream(client.getClientSocket().getOutputStream());
									SocketMsg kickoutMsg=new SocketMsg();
									kickoutMsg.setOperation("kick");
									kickoutMsg.setData("you have been kicked out by manager!");
									out.writeUTF(serializer.serialize(kickoutMsg));
									}catch(IOException e){
										System.out.println(e.getMessage());
									}
									parentFrm.getServerThread().getClients().remove(client);
									parentFrm.getUserListPanel().deleteUser(userArray.get(index));
									SocketMsg msg=new SocketMsg();
									msg.setOperation("deleteUser");
									msg.setData(client.getUserName());
									parentFrm.getCanvasPanel().sendMsg(msg);
									break;
									}
							}
						}
						}
						}
			        }
			}
		});
		scroll.setPreferredSize(new Dimension(200,100));
		scroll.setViewportView(userList);
		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
	}

	public ArrayList<String> getUserArray() {
		return userArray;
	}

	public void setUserArray(ArrayList<String> userArray) {
		this.userArray = userArray;
	}
	
	public void addUser(String newUser){
		if(!userArray.contains(newUser)){
		userArray.add(newUser);
		listModel.addElement(newUser);
		}
	}
	
	public void deleteUser(String delUser){
		int index=userArray.indexOf(delUser);
		if(index>0&&listModel.contains(delUser)){
		listModel.remove(index);
		userArray.remove(delUser);
		}
	}
	
	//clear userlist
	public void clear(){
		listModel.clear();
		userArray.clear();
	}
	
	

}
