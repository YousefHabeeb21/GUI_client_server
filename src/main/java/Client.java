import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;

	String list;

	ObjectOutputStream out;
	ObjectInputStream in;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){

		callback = call;
	}

	public void run() {

		try {
			socketClient= new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {

			try {

				String message = in.readObject().toString();
				//condition to allow current clients text field to update
				if(Objects.equals(message, "List")){
					list = in.readObject().toString();
				}
				else{
					callback.accept(message);
				}
			}
			catch(Exception e) {}
		}

	}
	public void send(String data) {
		try {
			// Send the message to the specified client
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getList(){
		return list;
	}
}
