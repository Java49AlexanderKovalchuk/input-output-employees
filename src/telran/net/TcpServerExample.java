package telran.net;
import java.net.*;
import java.io.*;
public class TcpServerExample {
static final int PORT=5000;
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server is listening to port " + PORT);
		while(true) {
			Socket socket = serverSocket.accept();
			clientRun(socket);
		}

	}
	private static void clientRun(Socket socket) {
		try(BufferedReader reader =
				new BufferedReader
				(new InputStreamReader(socket.getInputStream()));
				PrintStream writer = new PrintStream(socket.getOutputStream())) {
			while(true) {
				String line = reader.readLine();
				if(line == null) {
					System.out.println("client closed normally connection");
					break;
				}
				String response = getResponse(line);
				writer.println(response);
			}
			
		} catch (Exception e) {
			System.out.println("client closed abnormally connection");
		}
		
	}
	private static String getResponse(String line) {
		// <request type>#<string>
		//request type := "length" | "reverse"
		
		String response = "Wrong request structure, usage: <request type>#<string>";
		String [] tokens = line.split("#");
		if(tokens.length == 2) {
			response = switch(tokens[0]) {
			case "length" -> Integer.toString(tokens[1].length());
			case "reverse" -> new StringBuilder(tokens[1]).reverse().toString();
			default -> "wrong request type";
			};
		}
		
		return response;
	}

}
