package serveros;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBase
{
	public final Lock lockDB = new ReentrantLock(); 
	final Condition condition = lockDB.newCondition(); 

	public HashMap DB;
	public Stack<DBnode> stack;

	public FastThreadPool wThreadPool;
	public FastThreadPool MaintainStackThreadPool;
	public FastThreadPool UpdateMaxZThreadPool;

	public int C,MaxZ;
	CountDownLatch countDownLatch;

	public DataBase(int Y,Stack<DBnode> stack,int MaxZ,int C,CountDownLatch countDownLatch)
	{
		this.stack = stack;
		this.MaxZ = MaxZ;
		this.countDownLatch = countDownLatch;
		DB= new HashMap<Integer,DBnode>();
		wThreadPool = new FastThreadPool(1);
		MaintainStackThreadPool = new FastThreadPool(1);
	}
	public void startWorking(Integer x, int y)
	{
		lockDB.lock();
		Wthread wthread  = new Wthread(x, y);
		wThreadPool.enqueue(wthread);
		lockDB.unlock();
	}

	public void add(int x, int y)
	{
		lockDB.lock();
		DBnode node=new DBnode(x,y);
		if(DB.containsKey(x)==true)
		{
			incZ(x);
		}
		else
		{
			DB.put(x, node);
		}
		lockDB.unlock();
	}
	public void incZ(int x)
	{
		lockDB.lock();
		DBnode temp = (DBnode) DB.get(x);
		temp.incZ();

		DB.replace(x, temp);

		lockDB.unlock();
	}

	//	}
	@Override
	public String toString() {
		return "DataBase [DB=" + DB + "]";
	}

	public class Wthread extends Thread
	{
		private final Lock lock = new ReentrantLock(); 
		private int x,y;


		public Wthread(Integer x,int y)
		{
			this.x =x;
			this.y = y;
		}
		public void run()
		{
			lock.lock();
			if(y==-1)
			{
				incZ(x);
			}
			else//first appeareance:
			{
				add(x,y);
			}
			lock.unlock();
		}
	}
	public void runMaintainStackThread()
	{
		lockDB.lock();
		MaintainStackThread maintainStackThread = new MaintainStackThread();
		MaintainStackThreadPool.enqueue(maintainStackThread);
		lockDB.unlock();
	}

	public class MaintainStackThread extends Thread
	{
		private final Lock lock = new ReentrantLock(); 



		public MaintainStackThread()
		{

		}
		public void run()
		{
			//while(true)
			{
				try
				{
					Thread.sleep(100);
					Iterator it = DB.entrySet().iterator();
					while (it.hasNext())
					{
						Map.Entry pair = (Map.Entry)it.next();
						DBnode node = (DBnode) DB.get(pair.getKey());
						//System.out.println(node.getZ());
						if(node.getZ() >= MaxZ )
						{
							stack.push(node);
							//System.out.println("stack got: "+node.toString());
						}					
						countDownLatch.countDown();						
						countDownLatch.await();	
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				{

				}

			}
		}
	}


}


