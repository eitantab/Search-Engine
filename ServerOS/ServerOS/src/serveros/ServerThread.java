package serveros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class ServerThread extends Thread
{
	Socket socket;
	public DataBase dataBase;
	public Cache cache;
	int l;
	boolean ExistInCache;
	public Stack stack;
	

	public ServerThread(Socket socket,DataBase dataBase,int l,Cache cache,Stack<DBnode> stack)
	{
		this.socket = socket;
		this.dataBase = dataBase;
		this.cache = cache;
		this.l =l;
		this.ExistInCache  = false;
		this.stack = stack;

	}
	public void run()
	{
		int y=0;
		try
		{
			Integer message =null;

			ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
			//for (int i = 0; i < 1000; i++) 
			while(true)
			{
				dataBase.runMaintainStackThread();
		
				message= (Integer) inFromClient.readObject();
				//first check in the cache:
				
//					CallableSearchCache c1 = new CallableSearchCache(message);
//					Integer ans = c1.call();
//
//					if(ans == -1) 
//					{
//						//System.out.println("did not sent from the cache !!!");
//						ExistInCache =false;
//					}
//					else	//the num exist in the cache!
//					{
//						ExistInCache = true;
//						System.out.println("sent from the cache !!!");
//						outToClient.writeObject(ans);
//					}
				int ans =GotoCache(message);
				if(ExistInCache==true)
				{
					//System.out.println("sent from the cache !!!");
					outToClient.writeObject(ans);
				}
		
				y = GoToDataBase(message);

				if(ExistInCache==false)
				{
					outToClient.writeObject(y);
				}
			}
		}
		//socket.close();
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public int GotoCache(Integer message) throws Exception
	{
		CallableSearchCache c1 = new CallableSearchCache(message);
		Integer ans = c1.call();

		if(ans == -1) 
		{
			//System.out.println("did not sent from the cache !!!");
			ExistInCache =false;
		}
		else	//the num exist in the cache!
		{
			ExistInCache = true;
			
		}
		return ans;
	}
	public int GoToDataBase(Integer message) throws Exception
	{
		//create search thread:
		CallableSearchDB c1 = new CallableSearchDB(message);
		Boolean ans = c1.call();
		int y =0;

		if(ans==true)
		{
			this.dataBase.startWorking(message, -1); //y =i1 means that the number x already exist and z++	
			DBnode temp = (DBnode) dataBase.DB.get(message);
			y= temp.getY();
		}
		else
		{
			//send new task to w threadpool(1) queue
			y =(int)(Math.random()*l+1);
			this.dataBase.startWorking(message, y); //create new y => creat new DBnode
		}
		return y;
	}
	class CallableSearchDB implements Callable<Boolean>
	{
		private final Lock lock = new ReentrantLock(); 
		int x;
		public CallableSearchDB (Integer x)
		{
			this.x = x;
		}
		@Override
		public Boolean call() throws Exception
		{
			Boolean b =false;
			lock.lock();
			{
				b = dataBase.DB.containsKey(x);
			}
			lock.unlock();
			return b;
		}
	}
	class CallableSearchCache implements Callable<Integer>
	{
		private final Lock lock = new ReentrantLock(); 
		int x;
		public CallableSearchCache (Integer x)
		{
			this.x = x;
		}
		@Override
		public Integer call() throws Exception
		{
			Integer position;
			boolean exist;
			lock.lock();
			{
				
				//System.out.println("cach size: "+cache.cache.size()+cache.toString());
				//System.out.println("dataBase: "+dataBase.DB.size()+dataBase.DB.toString());
				//System.out.println(stack.size());
				exist = cache.cache.containsKey(x);
				if(exist==true)
				{
					DBnode temp =(DBnode)(cache.cache.get(x));
					position = temp.getX();
					
				}
				else
				{
					position =-1;
				}
			}
			lock.unlock();
			return position;
		}
	}
}


