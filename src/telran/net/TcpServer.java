package telran.net;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class TcpServer implements Runnable {
	static final int IDLE_TIMEOUT = 100;
	private int port;
	private ApplProtocol protocol;
	private ServerSocket serverSocket;
	AtomicInteger clientsCounter = new AtomicInteger(0);
	int nThreads = /*Runtime.getRuntime().availableProcessors()*/ 2;
	boolean isShutdown = false;
	ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
	
	public TcpServer(int port, ApplProtocol protocol) throws IOException {
		this.port = port;
		this.protocol = protocol;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(IDLE_TIMEOUT);
	}
	public void shutdown() {
		threadPool.shutdown();
		isShutdown = true;
	}
	@Override 
	public void run() {
		System.out.println("Server is listening on port " + port);
		
			while(!isShutdown) {
				try {
				Socket socket = serverSocket.accept();
				TcpClientServer1 clientServer = new TcpClientServer1(socket, protocol, this);
				clientsCounter.incrementAndGet(); 
				if(!isShutdown) {
					threadPool.execute(clientServer); 
				}
				
				}catch(SocketTimeoutException e) {
				
				}
				catch(Exception e) {
					e.printStackTrace();
					break;
				}
		} 
	}

}
