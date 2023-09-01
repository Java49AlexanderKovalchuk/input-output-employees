package telran.net;

import java.net.*;
import java.io.*;

public class TcpClientServer1 implements Runnable {
	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	ApplProtocol protocol;
	public TcpClientServer1(Socket socket, ApplProtocol protocol) throws IOException {
		this.socket = socket;
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
		this.protocol = protocol;
	}

	@Override
	public void run() {
		try {
			while(true) {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
				
			}
		} catch(EOFException e) {
			System.out.println("client closed normally connection");
		} catch(Exception e) {
			System.out.println("client closed abnormally connection "
		+ e.getMessage());
		}
		
	}

}
