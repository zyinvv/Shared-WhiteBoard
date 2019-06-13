package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import flexjson.JSONSerializer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JDialog;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.Choice;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ClientFrame {

	private JFrame frmClient;


	private ColorFrame colorFrame;
	private CanvasPanel canvasPanel;

	private JFileChooser fileChooser;
	private JDialog popUpDialog;// pop up when the user try to save an existed
								// file
	private JTextArea popUpMsg;
	private JButton yesBtn;
	private JButton noBtn;
	private CreateNetFrame netFrame;
	private ConnectNetFrame conNetFrame;
	private UserListPanel userListPanel;


	private File openedFile = null;// the opened file;
	private int userChoice = -1;
	private JTextField tfMsg;
	private JTextArea taMsgHis;
	private JMenuBar menuBar;

	private User user = null;
	private ArrayList<User> userList;
	private SocketThread serverThread = null;
	private SocketClient clientThread = null;
	private int serverPort = 23333;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame window = new ClientFrame();
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setTitle("Shared White Board - dalao & kengs");
		frmClient.setBounds(100, 100, 800, 800);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileChooser = new JFileChooser();

		netFrame = new CreateNetFrame(this);
		conNetFrame = new ConnectNetFrame(this);

		popUpDialog = new JDialog();
		popUpDialog.setMinimumSize(new Dimension(350, 175));
		popUpDialog.setModal(true);
		popUpDialog.setVisible(false);
		popUpMsg = new JTextArea();
		popUpMsg.setColumns(20);
		popUpMsg.setEditable(false);
		popUpMsg.setRows(3);

		yesBtn = new JButton();
		yesBtn.setText("yes");
		yesBtn.setSize(5, 2);
		yesBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				yesBtnClicked();
			}

		});
		noBtn = new JButton();
		noBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				noBtnClicked();
			}

		});
		noBtn.setSize(5, 2);
		noBtn.setText("no");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 259, 52, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		frmClient.getContentPane().setLayout(gridBagLayout);

		JPanel topPanel = new JPanel();
		GridBagLayout topLayout = new GridBagLayout();
		topLayout.columnWeights = new double[] { 1 };
		topPanel.setLayout(topLayout);
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.insets = new Insets(0, 0, 5, 0);
		gbc_topPanel.anchor = GridBagConstraints.WEST;
		gbc_topPanel.gridwidth = 11;
		gbc_topPanel.fill = GridBagConstraints.BOTH;
		gbc_topPanel.gridx = 0;
		gbc_topPanel.gridy = 0;
		frmClient.getContentPane().add(topPanel, gbc_topPanel);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints toolbarCon = new GridBagConstraints();
		toolbarCon.gridx = 0;
		toolbarCon.gridy = 0;
		toolbarCon.anchor = GridBagConstraints.WEST;
		topPanel.add(toolBar, toolbarCon);
		try {
			File imgFile = new File("images/icon_line.png");
			Image img = ImageIO.read(imgFile);
			JButton btnLine = new JButton();
			btnLine.setToolTipText("Line");
			btnLine.setIcon(new ImageIcon(img));
			btnLine.setSize(26, 26);
			btnLine.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("line");
				}
			});
			toolBar.add(btnLine);

			JButton btnCircle = new JButton();
			imgFile = new File("images/icon_circle.png");
			img = ImageIO.read(imgFile);
			btnCircle.setToolTipText("Circle");
			btnCircle.setIcon(new ImageIcon(img));
			btnCircle.setSize(26, 26);
			btnCircle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("circle");
				}
			});

			toolBar.add(btnCircle);
			JButton btnRect = new JButton();
			imgFile = new File("images/icon_rect.png");
			img = ImageIO.read(imgFile);

			btnRect.setToolTipText("Rectangle");
			btnRect.setIcon(new ImageIcon(img));
			btnRect.setSize(26, 26);
			btnRect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("rectangle");
				}
			});
			toolBar.add(btnRect);

			JButton btnOval = new JButton();
			imgFile = new File("images/icon_oval.png");
			img = ImageIO.read(imgFile);
			btnOval.setToolTipText("Oval");
			btnOval.setIcon(new ImageIcon(img));
			btnOval.setSize(26, 26);
			btnOval.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("oval");
				}
			});
			toolBar.add(btnOval);

			JButton btnPen = new JButton();
			imgFile = new File("images/icon_pen.png");
			img = ImageIO.read(imgFile);
			btnPen.setToolTipText("Pen");
			btnPen.setIcon(new ImageIcon(img));
			btnPen.setSize(26, 26);
			btnPen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("pen");
				}
			});
			toolBar.add(btnPen);

			JButton btnEraser = new JButton();
			imgFile = new File("images/icon_eraser.png");
			img = ImageIO.read(imgFile);
			btnEraser.setToolTipText("Eraser");
			btnEraser.setIcon(new ImageIcon(img));
			btnEraser.setSize(26, 26);
			btnEraser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("eraser");
				}
			});
			toolBar.add(btnEraser);

			JButton btnText = new JButton();
			imgFile = new File("images/icon_text.png");
			img = ImageIO.read(imgFile);
			btnText.setToolTipText("Text");
			btnText.setIcon(new ImageIcon(img));
			btnText.setSize(26, 26);
			btnText.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (canvasPanel != null)
						canvasPanel.setOperation("text");
				}
			});
			toolBar.add(btnText);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		JLabel lblPenSize = new JLabel("Pen Size");
		toolBar.add(lblPenSize);

		Choice pen_choice = new Choice();
		toolBar.add(pen_choice);

		JLabel lblEraserSize = new JLabel("eraser size");
		toolBar.add(lblEraserSize);

		Choice eraser_choice = new Choice();
		toolBar.add(eraser_choice);

		JButton btnColors = new JButton("change color");
		toolBar.add(btnColors);
		btnColors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorFrame.setVisible(true);
			}
		});
		eraser_choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				canvasPanel.setEraserSize(Integer.parseInt(eraser_choice.getSelectedItem()));
			}

		});
		pen_choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				canvasPanel.setStrokeSize(Integer.parseInt(pen_choice.getSelectedItem()));
			}

		});

		canvasPanel = new CanvasPanel(this);
		canvasPanel.setBackground(Color.WHITE);

		GridBagConstraints gbc_canvasPanel = new GridBagConstraints();
		gbc_canvasPanel.weighty = 7.0;
		gbc_canvasPanel.weightx = 7.0;
		gbc_canvasPanel.insets = new Insets(0, 0, 0, 5);
		gbc_canvasPanel.gridwidth = 9;
		gbc_canvasPanel.gridheight = 9;
		gbc_canvasPanel.fill = GridBagConstraints.BOTH;
		gbc_canvasPanel.gridx = 0;
		gbc_canvasPanel.gridy = 1;
		frmClient.getContentPane().add(canvasPanel, gbc_canvasPanel);

		colorFrame = new ColorFrame(canvasPanel);
		colorFrame.setTitle("color picker");
		colorFrame.setBounds(100, 100, 600, 400);
		colorFrame.setVisible(false);
		for (int i = 1; i <= 10; i++) {
			pen_choice.add(new Integer(i).toString());
		}
		for (int i = 1; i <= 10; i++) {
			eraser_choice.add(new Integer(i).toString());
		}

		menuBar = new JMenuBar();
		frmClient.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("file");
		menuBar.add(mnFile);

		JMenuItem newMenu = new JMenuItem("New");
		newMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				newMenuClicked(e);
			}
		});
		mnFile.add(newMenu);

		JMenuItem openMenu = new JMenuItem("Open");
		openMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				openMenuClicked(e);
			}
		});
		mnFile.add(openMenu);

		JMenuItem saveMenu = new JMenuItem("Save");
		saveMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveMenuClicked(e);
			}
		});
		mnFile.add(saveMenu);

		JMenuItem saveAsMenu = new JMenuItem("Save as");
		saveAsMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveAsMenuClicked(e);
			}
		});
		mnFile.add(saveAsMenu);

		JMenuItem closeMenu = new JMenuItem("Close");
		closeMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				closeMenuClicked(e);
			}
		});
		mnFile.add(closeMenu);

		JMenu mnNetwork = new JMenu("network");
		menuBar.add(mnNetwork);

		JMenuItem createNetMenu = new JMenuItem("create a network");
		createNetMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createNetClicked(e);
			}
		});
		mnNetwork.add(createNetMenu);

		JMenuItem connectMenu = new JMenuItem("connect to a network");
		connectMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				conNetClicked(e);
			}
		});
		mnNetwork.add(connectMenu);
		
		JMenuItem endNetMenu = new JMenuItem("disconnect from network");
		endNetMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				endNetClicked(e);
			}
		});
		mnNetwork.add(endNetMenu);


		userListPanel = new UserListPanel(this);
		userListPanel
				.setBorder(new TitledBorder(null, "Online Users", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagConstraints gbc_listPanel = new GridBagConstraints();
		gbc_listPanel.insets = new Insets(0, 0, 5, 0);
		gbc_listPanel.fill = GridBagConstraints.BOTH;
		gbc_listPanel.gridwidth = 2;
		gbc_listPanel.gridheight = 3;
		gbc_listPanel.weighty = 3.0;
		gbc_listPanel.weightx = 2.0;
		gbc_listPanel.gridx = 9;
		gbc_listPanel.gridy = 1;
		
		frmClient.getContentPane().add(userListPanel, gbc_listPanel);

		taMsgHis = new JTextArea("");
		taMsgHis.setEditable(false);
		JScrollPane msgPane = new JScrollPane(taMsgHis);
		msgPane.setViewportBorder(
				new TitledBorder(null, "Message History", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_msgPane = new GridBagConstraints();
		gbc_msgPane.insets = new Insets(0, 0, 5, 0);
		gbc_msgPane.weighty = 5.0;
		gbc_msgPane.weightx = 1.5;
		gbc_msgPane.gridwidth = 2;
		gbc_msgPane.gridheight = 5;
		gbc_msgPane.fill = GridBagConstraints.BOTH;
		gbc_msgPane.gridx = 9;
		gbc_msgPane.gridy = 4;
		frmClient.getContentPane().add(msgPane, gbc_msgPane);

		tfMsg = new JTextField();
		GridBagConstraints gbc_tfMsg = new GridBagConstraints();
		gbc_tfMsg.insets = new Insets(0, 0, 0, 5);
		gbc_tfMsg.weightx = 1.3;
		gbc_tfMsg.weighty = 1.0;
		gbc_tfMsg.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfMsg.gridx = 9;
		gbc_tfMsg.gridy = 9;
		
		tfMsg.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                sendMsgClicked();
            }  
        });
		frmClient.getContentPane().add(tfMsg, gbc_tfMsg);
		tfMsg.setColumns(10);

		JButton btnSendMsg = new JButton("send");
		GridBagConstraints gbc_btnSendMsg = new GridBagConstraints();
		gbc_btnSendMsg.weighty = 1.0;
		gbc_btnSendMsg.weightx = 0.5;
		gbc_btnSendMsg.gridx = 10;
		gbc_btnSendMsg.gridy = 9;
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsgClicked();
			}
		});
		frmClient.getContentPane().add(btnSendMsg, gbc_btnSendMsg);
		userList = new ArrayList<User>();
	}

	public void newMenuClicked(MouseEvent e) {
        Object[] options = { "Yes", "No" };
		int decision = JOptionPane.showOptionDialog(frmClient,
				 "Do you want to create a new canvas", "Tips", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if(decision==JOptionPane.OK_OPTION){
		canvasPanel.reset();
		canvasPanel.repaint();
		//broadcast new command 
		if(serverThread!=null){
			SocketMsg msg=new SocketMsg();
			msg.setOperation("newCanvas");
			canvasPanel.sendMsg(msg);
		}
		}
		
	}

	public void openMenuClicked(MouseEvent e) {
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new CanvasFileFilter(CanvasFileFilter.CanvasFilterType.COF_OPEN));
		if (fileChooser.showOpenDialog(frmClient) == 0) {
			File f = fileChooser.getSelectedFile();
			if (f.exists()) {
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(f.getPath()));
					List<CanvasShape> sh = (List<CanvasShape>) in.readObject();
					in.close();
					canvasPanel.reset();
					canvasPanel.repaint();
					canvasPanel.setShapes((CopyOnWriteArrayList<CanvasShape>) sh);
					canvasPanel.repaint();
					openedFile = fileChooser.getSelectedFile();
					
					//clear all clients' canvas  
					if(serverThread!=null){
						SocketMsg msg=new SocketMsg();
						msg.setOperation("newCanvas");
						canvasPanel.sendMsg(msg);
					}
					for(CanvasShape shape:sh){
						SocketMsg msg=new SocketMsg();
						msg.setOperation("add");
						JSONSerializer serializer = new JSONSerializer();
						String data = serializer.serialize(shape);
						msg.setData(data);
						canvasPanel.sendMsg(msg);
					}
					
					
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}
		}

		fileChooser.resetChoosableFileFilters();
	}

	public void saveMenuClicked(MouseEvent e) {
		if (openedFile == null) {
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new CanvasFileFilter(CanvasFileFilter.CanvasFilterType.COF_SAVE));
			if (fileChooser.showSaveDialog(frmClient) == 0) {
				File f = fileChooser.getSelectedFile();
				if (f.exists()) {
					showFileExistDlg(f);
				} else {
					userChoice = 1;
				}
				if (userChoice == 1) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f.getPath()));
						out.writeObject(canvasPanel.getShapes());
						out.close();
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				userChoice = -1;
			}
		} else {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(openedFile.getPath()));
				out.writeObject(canvasPanel.getShapes());
				out.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		fileChooser.resetChoosableFileFilters();
	}

	public void saveAsMenuClicked(MouseEvent e) {
		CanvasFileFilter filter = new CanvasFileFilter(CanvasFileFilter.CanvasFilterType.SAVE);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		if (fileChooser.showSaveDialog(frmClient) == 0) {
			try {
				File f = fileChooser.getSelectedFile();
				if (f.exists()) {
					showFileExistDlg(f);

				} else {
					userChoice = 1;
				}
				if (userChoice == 1) {

					String ext = filter.getExtension(f.getPath());
					java.awt.image.BufferedImage img = canvasPanel.getImage();
					javax.imageio.ImageIO.write(img, ext, f);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			userChoice = -1;
		}

		fileChooser.resetChoosableFileFilters();

	}

	public void closeMenuClicked(MouseEvent e) {
		endNetClicked(e);
		frmClient.dispose();
		System.exit(0);
	}

	public void showFileExistDlg(File f) {
		double xpos = frmClient.getLocationOnScreen().getX() + frmClient.getSize().getWidth() / 2.0D
				- popUpDialog.getSize().getWidth() / 2.0D;
		double ypos = frmClient.getLocationOnScreen().getY() + frmClient.getSize().getHeight() / 2.0D
				- popUpDialog.getSize().getHeight() / 2.0D;
		popUpDialog.setLocation((int) xpos, (int) ypos);
		popUpMsg.setText("File : \"" + f.getName() + "\" already exists.\nDo you want to overwrite?");
		popUpDialog.setTitle("Overwrite Confirmation");
		GridBagConstraints msgCon = new GridBagConstraints();
		msgCon.gridx = 0;
		msgCon.gridy = 0;
		msgCon.gridwidth = 2;
		msgCon.gridheight = 1;
		GridBagConstraints yesBtnCon = new GridBagConstraints();
		yesBtnCon.gridx = 0;
		yesBtnCon.gridy = 1;
		// yesBtnCon.gridwidth=1;
		yesBtnCon.fill = GridBagConstraints.NONE;
		GridBagConstraints noBtnCon = new GridBagConstraints();
		noBtnCon.gridx = 1;
		noBtnCon.gridy = 1;
		// noBtnCon.gridwidth=1;
		noBtnCon.fill = GridBagConstraints.NONE;
		GridBagLayout gridLayout = new GridBagLayout();
		popUpDialog.getContentPane().setLayout(gridLayout);
		popUpDialog.getContentPane().add(popUpMsg, msgCon);
		popUpDialog.getContentPane().add(yesBtn, yesBtnCon);
		popUpDialog.getContentPane().add(noBtn, noBtnCon);
		popUpDialog.setVisible(true);
	}

	// to be a server for other peers to join in
	public void createNetClicked(MouseEvent e) {
		double x_pos = (frmClient.getBounds().getMinX() + frmClient.getBounds().getMaxX()) / 7 * 2;
		double y_pos = (frmClient.getBounds().getMinY() + frmClient.getBounds().getMaxY()) / 5 * 2;
		netFrame.setLocation((int) x_pos, (int) y_pos);
		netFrame.setVisible(true);
		if (netFrame.isValid() == true) {
			serverPort = netFrame.getServerPort();

		}
	}

	public void conNetClicked(MouseEvent e) {
		double x_pos = (frmClient.getBounds().getMinX() + frmClient.getBounds().getMaxX()) / 7 * 2;
		double y_pos = (frmClient.getBounds().getMinY() + frmClient.getBounds().getMaxY()) / 5 * 2;
		conNetFrame.setLocation((int) x_pos, (int) y_pos);
		conNetFrame.setVisible(true);
	}
	
	public void endNetClicked(MouseEvent e){
		if(serverThread!=null||clientThread!=null){
		Object[] options = { "end anyway", "nope" };
		int decision = JOptionPane.showOptionDialog(frmClient,
				"would you like to disconnect", "disconnect from network", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if(decision==JOptionPane.YES_OPTION){
			//if is server ,close all socket and broadcast
			if(serverThread!=null){
				SocketMsg msg=new SocketMsg();
				msg.setOperation("disconnect");
				msg.setData("the manager has disconnected");
				canvasPanel.sendMsg(msg);
				
				//close all client socket
				CopyOnWriteArrayList<User> clients=serverThread.getClients();
				try{
				for(User client:clients){
					
						if(client.getClientSocket()!=null){
							client.getClientSocket().close();
						}
					
					
				}
				serverThread.getCanvasServer().close();
				serverThread=null;
				user=null;
				}catch(IOException ioe){
						System.out.println(ioe.getMessage());
					}
				clients.removeAll(clients);
				
				
				
			}
			
			//if is client, disconnect directly
			else if(clientThread!=null){
				try{
				clientThread.getClient().close();
				clientThread=null;
				getMenuBar().getMenu(0).setEnabled(true);
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
			
			getUserListPanel().clear();
		}
		}
		else{
			JOptionPane.showMessageDialog(frmClient,"you don't have a network");
		}
	}

	public void yesBtnClicked() {
		userChoice = 1;
		popUpDialog.setVisible(false);
	}

	public void noBtnClicked() {
		userChoice = 0;
		popUpDialog.setVisible(false);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CanvasPanel getCanvasPanel() {
		return canvasPanel;
	}

	public void setCanvasPanel(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;
	}

	public SocketThread getServerThread() {
		return serverThread;
	}

	public void setServerThread(SocketThread serverThread) {
		this.serverThread = serverThread;
	}

	public SocketClient getClientThread() {
		return clientThread;
	}

	public void setClientThread(SocketClient clientThread) {
		this.clientThread = clientThread;
	}

	public JTextArea getTaMsgHis() {
		return taMsgHis;
	}

	public void setTaMsgHis(JTextArea taMsgHis) {
		this.taMsgHis = taMsgHis;
	}

	public void sendMsgClicked() {
		if(serverThread==null&&clientThread==null){
            JOptionPane.showMessageDialog(frmClient, "please start or connect to a network first",  
                    "error", JOptionPane.ERROR_MESSAGE);  
		}
		else{
		if (tfMsg.getText().trim().equals("")) {
			// no message is input
		} else {
			String msgContent = tfMsg.getText();
			SocketMsg msg = new SocketMsg();
			msg.setOperation("chat");
			msg.setData(msgContent);
			msg.setUserName(this.getUser().getUserName());
			JSONSerializer serializer = new JSONSerializer();
			String msgJson = serializer.serialize(msg);

			// server send msg to all clients
			if (serverThread != null) {
				CopyOnWriteArrayList<User> clients = getServerThread().getClients();
				if (!clients.isEmpty()) {
					int i=0;
					for (User client : clients) {
						if (i==0){
							i++;
							continue;
						}
						try {
							DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
							out.writeUTF(msgJson);
						} catch (IOException e) {
							System.out.println(e.getMessage());
						}
					}
				}
				// show message in own interface

			}
			// client send msg to server
			else if (clientThread != null) {
				try {
					DataOutputStream out = new DataOutputStream(getClientThread().getClient().getOutputStream());
					out.writeUTF(msgJson);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			taMsgHis.append(user.getUserName()+"(me)" + ": " + msgContent + "\n");
			tfMsg.setText("");
		}

	}
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}
	
	public JFrame getFrmClient() {
		return frmClient;
	}
	public UserListPanel getUserListPanel() {
		return userListPanel;
	}

	public void setUserListPanel(UserListPanel userListPanel) {
		this.userListPanel = userListPanel;
	}
}
