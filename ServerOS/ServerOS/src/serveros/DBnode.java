package serveros;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBnode implements Comparable<DBnode>
{
	private final Lock lock = new ReentrantLock(); 
	private int x;
	private int y;
	private int z;
	

	//consturcor for case that y not availabe
	public DBnode(int x,int y)
	{
		this.x = x;
		this.y = y;
		this.z = 1;
	}
	
	public void incZ()
	{
		lock.lock();
		this.z++;
		lock.unlock();
	}

	@Override
	public String toString() {
		return "DBnode [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	int getX()
	{
		return x;
	}
	int getY()
	{
		return y;
	}
	int getZ()
	{
		return z;
	}

	@Override
	public int compareTo(DBnode o) {
		
		return Integer.compare(this.z,o.z);
	}
}
