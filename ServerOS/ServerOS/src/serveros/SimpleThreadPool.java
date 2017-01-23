package serveros;

import java.util.LinkedList;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
//my way without sincronized:
import java.util.concurrent.locks.Lock;


public class SimpleThreadPool
{


	private WorkerThread[] threads;
	private LinkedList<Runnable> taskQueue;
	//our sincronized:
	private final Lock lock = new ReentrantLock(); 
	final Condition notEmpty = lock.newCondition(); 

	public SimpleThreadPool(int threadNumber) {

		taskQueue = new LinkedList<Runnable>();
		threads = new WorkerThread[threadNumber];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WorkerThread();
			threads[i].start();	
		}
	}
	//add task to thread queue
	public void enqueue(Runnable r) {
		
		lock.lock();
		{
			taskQueue.addLast(r);
			notEmpty.signal(); //signal = notify
		}
		lock.unlock();
	}


	public class WorkerThread extends Thread {

		
		public void run()
		{
			Runnable r;
			//take out the thread to start doing the task:
			while (true)
			{
				lock.lock();
				{ 
					while (taskQueue.isEmpty())
					{
						{
							//if the queue is empty, wait.
							try 
							{
								notEmpty. await();
							} 
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						} 
					}
					//queue is not empty =>
					r = (Runnable) taskQueue.removeFirst();					
				}
				try
				{
					Thread.sleep(1);
					((Thread) r).start();
					//System.out.println("started by "+this.getName());
				} catch (Exception e) 
				{
					// ignore
				}
				finally
				{
				lock.unlock();
				}
			}

		}
	}
}