
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server {

	int count = 1;
	int[] recipientNumbers;
	String recipient;
	String message;

	ArrayList<Integer> invalidClients = new ArrayList<Integer>();
	ArrayList<Integer> clientNum = new ArrayList<Integer>();
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;


	Server(Consumer<Serializable> call) {

		callback = call;
		server = new TheServer();
		server.start();
	}


	public class TheServer extends Thread {

		public void run() {

			try (ServerSocket mysocket = new ServerSocket(5555);) {
				System.out.println("Server is waiting for a client!");


				while (true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("client has connected to server: " + "client #" + count);
					clients.add(c);
					clientNum.add(count);
					c.start();
					count++;
				}
			}//end of try
			catch (Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}


	class ClientThread extends Thread {

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		ClientThread(Socket s, int count) {
			this.connection = s;
			this.count = count;
		}



		// sends the message to every client using the loop and count
		public void updateClients(String message) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					// send updated list of clients to each client
					t.out.writeObject(message);
				} catch (Exception e) {}
			}
		}


		// send the message to only the clients that match the client variables numerical value
		public void updateSpecificClients(String message, int client) {

			boolean clientExists = false;
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				if(t.count == (client)) {
					try {
						// send updated list of clients to each client
						t.out.writeObject("client #" + count + " said: " + message);
						out.writeObject("you said to client #" + client + ": " + message);
						clientExists = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// send an error message if the client does not exist
			if (!clientExists) {
				invalidClients.add(client);
				try {
					out.writeObject("Error: client #" + client + " does not exist.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}



		// send message to the sender
		public void updateSelf(String message,  int client) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				if(t.count == (client)) {
					try {
						// send updated list of clients to each client
						t.out.writeObject(message);
					} catch (Exception e) {e.printStackTrace();}
				}
			}
		}

		// updates the Current clients TextField
		public void updateClientList() {
			StringBuilder clientList = new StringBuilder("current clients: ");
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				clientList.append("client #").append(t.count).append(", ");
			}
			String newClientlist = clientList.substring(0, clientList.length() - 1);

			updateClients("List");
			updateClients(newClientlist);
		}

		// separartes the message and the recipients that were combined into a single string in the client controller
		private void parse(String data){
			String[] dataParts = data.split(":");
			message = dataParts[0];
			recipient = dataParts[1];
		}
		// takes the integers in the message box and adds them to an integer array
		private void parse2() {
			String[] recipientParts = recipient.split(",");

			recipientNumbers = new int[recipientParts.length];
			for (int i = 0; i < recipientParts.length; i++) {
				recipientNumbers[i] = Integer.parseInt(recipientParts[i]);
			}
		}


		public void run() {

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);

				// notify other clients of new client connection
				updateClients("new client on server: client #" + count);
				updateClientList();

			} catch (Exception e) {
				System.out.println("Streams not open");
			}

			while (true) {
				try {
					String data = in.readObject().toString();
					parse(data);

					if (Objects.equals(recipient, "all") || Objects.equals(recipient, "All") || Objects.equals(recipient, "ALL")) {
						updateClients("client #" + count + " said: " + message);
					} else {
						// if the recipient is not "all" then we will check what client numbers there are
						parse2();
						for (int i = 0; i < recipientNumbers.length; i++) {

							// send message to both sender and recipient if they are the same client
							if (recipientNumbers[i] == count) {
								updateSelf("you sent a message to yourself: " + message, count);
							}
							else {
								// other individual clients
								updateSpecificClients(message, recipientNumbers[i]);
							}
						}
					}

					callback.accept("client: " + count + " sent: " + message);

					// print error messages for invalid clients to the server
					if(!invalidClients.isEmpty()){
						StringBuilder invalid = new StringBuilder();

						for (int item : invalidClients) {
							invalid.append(Integer.toString(item));
							invalid.append(", ");
						}
						String invalidNum = invalid.substring(0, invalid.length() - 1);

						callback.accept("Error:  These clients:   " + invalidNum + " do not exist.");
						invalidClients.clear();
					}

				}

				catch (Exception e) {
					callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					updateClients("Client #" + count + " has left the server!");
					clients.remove(this);
					clientNum.remove(count);
					count --;
					updateClientList();
					break;
				}
			}
		}
	}//end of client thread
}







