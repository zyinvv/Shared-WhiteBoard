package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class SocketThread extends Thread {
	private CopyOnWriteArrayList<User> clients;
	// the server will be added to as the first user
	private int serverPort = 23333;
	private ServerSocket canvasServer;
	private ClientFrame rootFrm;

	SocketThread(int serverPort, ClientFrame frmClient) {
		this.setServerPort(serverPort);
		rootFrm = frmClient;
		clients = new CopyOnWriteArrayList<User>();
		this.start();
		rootFrm.setServerThread(this);
		clients.add(rootFrm.getUser());
	}

	public void run() {
		try {
			if (canvasServer == null && rootFrm.getClientThread() == null) {
				canvasServer = new ServerSocket(serverPort);
				System.out.println("Server is on");
				while (true) {
					Socket clientSocket = canvasServer.accept();
					new ClientThread(clientSocket);
				}
			}
		} catch (Exception e) {
			System.out.println("serverSocket:" + e.getMessage());
		} finally {
			try {
				if (canvasServer != null)
					canvasServer.close();
			} catch (IOException e) {

			}
		}
	}

	public ServerSocket getCanvasServer() {
		return canvasServer;
	}

	public void setCanvasServer(ServerSocket canvasServer) {
		this.canvasServer = canvasServer;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public boolean checkUserName(String userName) {
		if (rootFrm.getUser().getUserName().equals(userName))
			return false;
		for (User client : clients) {
			if (client.getUserName().equals(userName))
				return false;
		}
		return true;
	};

	public String msgToJson(SocketMsg msg) {
		JSONSerializer serializer = new JSONSerializer();
		return serializer.serialize(msg);
	}

	public class ClientThread extends Thread {
		private Socket clientSocket;
		private User user;// user information of the connected client
		private DataInputStream in;
		private DataOutputStream out;

		public ClientThread(Socket clientSocket) {
			this.setClientSocket(clientSocket);
			try {
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
				String userName = in.readUTF();// read the user name of client;
				user = new User(userName, clientSocket, true);
				if (checkUserName(userName)) {
					Object[] options = { "Approve", "Reject" };
					int decision = JOptionPane.showOptionDialog(rootFrm.getFrmClient(),
							userName + " would like to connect", "new connection", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (decision == JOptionPane.OK_OPTION) {
						clients.add(user);
						rootFrm.getUserListPanel().addUser(userName);
						updateUserAll(userName, "addUser");
						// send current shapes to the connected clients
						ArrayList<CanvasShape> shapes = new ArrayList<CanvasShape>();
						shapes.addAll(rootFrm.getCanvasPanel().getShapes());
						for (CanvasShape shape : shapes) {
							SocketMsg packet = new SocketMsg();
							packet.setOperation("add");
							packet.setUserName(rootFrm.getUser().getUserName());
							JSONSerializer serializer = new JSONSerializer();
							String data = serializer.serialize(shape);
							packet.setData(data);
							out.writeUTF(serializer.serialize(packet));
						}

						// send current userList to the connected clients
						sendAllUser();
						this.start();
					} else {
						SocketMsg msg = new SocketMsg();
						msg.setOperation("error");
						msg.setData("The manager rejected your connection");
						out.writeUTF(msgToJson(msg));
						clientSocket.close();
					}
				}
				// invalid user name
				else {
					SocketMsg msg = new SocketMsg();
					msg.setOperation("error");
					msg.setData("Duplicated User Name in system, please user another one");
					out.writeUTF(msgToJson(msg));
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				if (clientSocket != null) {

					try {
						clientSocket.close();
					} catch (IOException ioe) {
						System.out.println(ioe.getMessage());
					}
				}
			}

		}

		public void run() {
			while (true) {
				try {
					String data = in.readUTF();
					JSONDeserializer<SocketMsg> parser = new JSONDeserializer<SocketMsg>();
					SocketMsg msg = parser.deserialize(data);
					// String shapeStr=msg.getData();
					if (msg.getOperation().equals("add")) {

						CanvasShape newShape = new JSONDeserializer<CanvasShape>().deserialize(msg.getData());
						rootFrm.getCanvasPanel().addOneShape(newShape);
						rootFrm.getCanvasPanel().repaint();

					}
					if (msg.getOperation().equals("chat")) {
						rootFrm.getTaMsgHis().append(msg.getUserName() + ": " + msg.getData() + "\n");
					}
					BroadCast(data, msg.getUserName());
				} catch (IOException e) {
					System.out.println(e.getMessage());
					if (clientSocket != null) {
						clients.remove(user);
						rootFrm.getUserListPanel().deleteUser(user.getUserName());
						updateUserAll(user.getUserName(), "deleteUser");
						try {
							clientSocket.close();
						} catch (IOException ioe) {
							System.out.println(ioe.getMessage());
						}
					}
					break;
				}
			}
		}

		// broadcast to all user excluding the sender
		public void BroadCast(String msgJson, String excludedUser) {

			if (!clients.isEmpty()) {
				int i = 0;
				for (User client : clients) {
					if (i == 0) {
						i++; // the first client is the server
						continue;
					}
					try {
						if (!client.getUserName().equals(excludedUser)) {
							DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
							out.writeUTF(msgJson);
						}
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					i++;
				}
			}
		}

		public DataInputStream getIn() {
			return in;
		}

		public void setIn(DataInputStream in) {
			this.in = in;
		}

		public DataOutputStream getOut() {
			return out;
		}

		public void setOut(DataOutputStream out) {
			this.out = out;
		}

		public Socket getClientSocket() {
			return clientSocket;
		}

		public void setClientSocket(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		// send all users to the connected client
		public void sendAllUser() {
			try {

				for (User client : clients) {

					DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
					SocketMsg packet = new SocketMsg();
					JSONSerializer serializer = new JSONSerializer();
					packet.setOperation("addUser");
					packet.setUserName(rootFrm.getUser().getUserName());
					if (client.isServer())
						packet.setData(client.getUserName() + "(manager)");
					else
						packet.setData(client.getUserName());
					out.writeUTF(serializer.serialize(packet));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// add or delete a user in all clients
		public void updateUserAll(String userName, String option) {
			try {
				int i = 1;// index 0 is for server
				for (; i < clients.size(); i++) {
					DataOutputStream out = new DataOutputStream(clients.get(i).getClientSocket().getOutputStream());
					for (User client : clients) {

						SocketMsg packet = new SocketMsg();
						JSONSerializer serializer = new JSONSerializer();
						packet.setOperation(option);
						packet.setUserName(rootFrm.getUser().getUserName());
						if (client.isServer())
							packet.setData(client.getUserName() + "(manager)");
						else
							packet.setData(client.getUserName());
						out.writeUTF(serializer.serialize(packet));
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public CopyOnWriteArrayList<User> getClients() {
		return clients;
	}

}
