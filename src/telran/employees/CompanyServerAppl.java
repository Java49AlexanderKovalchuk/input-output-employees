package telran.employees;

import java.io.IOException;
import java.util.Scanner;

import telran.employees.service.*;
import telran.net.TcpServer;

public class CompanyServerAppl {

	private static final String DEFAULT_FILE_NAME = "employees.data";
	private static String fileName ;
	private static int PORT = 5000;

	public static void main(String[] args) throws IOException {
		fileName = args.length > 0 ? args[0] : DEFAULT_FILE_NAME;
		Company company = new CompanyImpl();
		company.restore(fileName );
		TcpServer tcpServer = new TcpServer(PORT , new CompanyProtocol(company));
		Thread thread = new Thread(tcpServer);
		thread.start(); 
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter 'exit' for server shutdown");
		while(!scanner.nextLine().equalsIgnoreCase("exit")) {
			System.out.println("for shutdown enter should be 'exit'");
		}
		tcpServer.shutdown();
		company.save(DEFAULT_FILE_NAME);

}
}
