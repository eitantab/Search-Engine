package serveros;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Cache {

    public final Lock lockDB = new ReentrantLock();
    final Condition condition = lockDB.newCondition();

    public HashMap cache;
    public Stack<DBnode> stack;

    public FastThreadPool CacheThreadPool;

    public int C, M, MaxZ;
    CountDownLatch countDownLatch;

    public Cache(int C, int M, Stack<DBnode> stack, int MaxZ, CountDownLatch countDownLatch) {
        this.C = C;
        this.M = M;
        this.MaxZ = MaxZ;
        this.stack = stack;
        this.countDownLatch = countDownLatch;;
        cache = new HashMap<Integer, DBnode>();
        CacheThreadPool = new FastThreadPool(1);

    }

    public void startWorking() {
        lockDB.lock();
        CacheThread cacheThread = new CacheThread();
        CacheThreadPool.enqueue(cacheThread);
        lockDB.unlock();
        //stack.add(new DBnode(1, 1));

    }

    public boolean isEmpty() {
        if (cache.isEmpty() == true) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Cache [Cache=" + cache + "]";
    }

    public class CacheThread extends Thread {

        private final Lock lock = new ReentrantLock();

        public CacheThread() {

        }

        public void run() {
            while (true) {
                try {
                    countDownLatch.await();
                    {
                        while (stack.isEmpty() != true) {
                            DBnode node = stack.pop();
                            //System.out.println("stack got: "+node.toString());
                            int temp = node.getZ();
                            if (temp >= MaxZ && cache.size() < C) {
                                cache.put(node.getX(), node);
                                //System.out.println("cache got: "+node.toString());
                            }
                            if (temp >= MaxZ && cache.size() > C) {
                                cache.replace(node.getX(), node);
                            }
                        }
                        int sum = 0;
                        Iterator it = cache.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            DBnode node = (DBnode) cache.get(pair.getKey());
                            sum = sum + node.getZ();
                        }
                        MaxZ = sum / cache.size();
                        //System.out.println("MaxZ = "+MaxZ);
                        countDownLatch.countDown();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
