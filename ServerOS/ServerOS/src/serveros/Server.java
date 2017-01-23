package serveros;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Server extends Thread
{
	private static final int PORT = 4444;
	private ServerSocket serverSocket;
	private Socket socket;
	private SimpleThreadPool stp;
	private int numOfthread;
	private int l,Y,C,M;		//l = calculate random number if no answer available, rand(1,l)
	private Integer MaxZ;
	private DataBase dataBase;
	private Cache cache;
	private Stack<DBnode> stack ;
	private CountDownLatch countDownLatch;

	
	public Server(int numOfThreads,int l,int Y,int M,int C)
	{
	this.numOfthread = numOfThreads;
	this.l = l;
	this.C =C; 		//num of the max element in the cache
	this.M = M;		// M is minimum of z that may allow to be saved in the cache
	this.MaxZ = M;
	this.countDownLatch  = new CountDownLatch(1); 
	this.stack = new Stack<DBnode>();
	this.dataBase  = new DataBase(Y,stack,MaxZ,C,countDownLatch);
	this.cache = new Cache(C, M,stack,MaxZ,countDownLatch);
	
	
	
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server up and ready for connection...");
			stp = new SimpleThreadPool(numOfthread);
			cache.startWorking();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
	}
	
	public void run()
	{
		while(true)
		{
			Socket socket;
			try {
				
				socket = serverSocket.accept();
				//new ServerThread(socket).start();
				
				stp.enqueue(new ServerThread(socket,dataBase,l,cache,stack));
		
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}

