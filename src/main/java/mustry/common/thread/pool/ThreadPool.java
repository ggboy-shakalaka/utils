package mustry.common.thread.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPool {
	private static byte RUN = 1;
	private static byte PAUSE = 1 << 1;
	private static byte STOP = 1 << 2;
	
	private Queue<Runnable> queue;
	private int pool_size = 0;
	private byte status = 1;
	private int max_pool_size = 5;
	private int timeout = 1000 * 10;
	private int ability = 5; // 表示单个线程所能处理的能力上线
	
	public ThreadPool() {
		this.queue = new ConcurrentLinkedQueue<Runnable>();
	}
	
	public ThreadPool(int maxSize, int timeout, int ability, int status) {
		if ((this.status & ThreadPool.STOP) != 0) {
			throw new RuntimeException("thread pool status can not be stop");
		}
		this.queue = new ConcurrentLinkedQueue<Runnable>();
		this.status = (byte) status;
		this.timeout = timeout;
		this.max_pool_size = maxSize;
		this.ability = ability;
	}

	public void push(Runnable runnable) {
		if ((this.status & ThreadPool.STOP) != 0) {
			throw new RuntimeException("thread pool is stop");
		}
		
		this.queue.offer(runnable);
		if ((this.status & ThreadPool.RUN) != 0)
			if (ThreadPool.needNewThread(pool_size, queue.size(), ability, max_pool_size))
				new ThreadExecute().start();
	}
	
	public synchronized void start() {
		if ((this.status & ThreadPool.RUN) != 0) {
			throw new RuntimeException("thread pool is run");
		}
		if ((this.status & ThreadPool.STOP) != 0) {
			throw new RuntimeException("thread pool is stop");
		}
		
		int poolSize = ThreadPool.needThreadSize(pool_size, queue.size(), ability, max_pool_size);
		for (int i = 0; i< poolSize; i++) {
			new ThreadExecute().start();
		}
		this.status = ThreadPool.RUN;
	}
	
	public synchronized void pause() {
		if ((this.status & ThreadPool.STOP) != 0) {
			throw new RuntimeException("thread pool is stop");
		}
		if ((this.status & ThreadPool.PAUSE) != 0) {
			throw new RuntimeException("thread pool is pause");
		}
		this.status = ThreadPool.PAUSE;
	}
	
	public synchronized void stop() {
		if ((this.status & ThreadPool.STOP) != 0) {
			throw new RuntimeException("thread pool is stop");
		}
		this.status = ThreadPool.STOP;
		queue.clear();
	}

	class ThreadExecute extends Thread {
		@Override
		public void run() {
			startupThread();
			long begin = System.currentTimeMillis();
			while (true) {
				if (!isRun())
					return;
				Runnable runnable = getTask();
				if (runnable == null) {
					if (System.currentTimeMillis() - begin > timeout) {
						break; // 线程获取任务时间超时，自动关闭
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					continue;
				}
				runnable.run();
				begin = System.currentTimeMillis();
			}
			shutdownThread();
		}
	}
	

	private Runnable getTask() {
		return this.queue.poll();
	}
	
	private synchronized void shutdownThread() {
		pool_size--;
	}
	
	private synchronized void startupThread() {
		pool_size++;
	}
	
	/*
	 * 当线程执行完任务进行isRun判断为pause 但未调用 pool的shutdownThread时，此时调用start方法
	 * 由于pool_size 没有减少，将会忽略掉这个将要结束但暂时未结束的线程，从而创建的线程比预期少1
	 * 所以在此添加减少pool_size 解决问题
	 */
	private synchronized boolean isRun() {
		if ((status & ThreadPool.RUN) != 0) {
			return true;
		}
		shutdownThread();
		return false;
	}
	
	private static boolean needNewThread(int poolSize, int queueSize, int ability, int max_pool_size) {
		return (poolSize * ability < queueSize) && poolSize < max_pool_size;
	}
	
	private static int needThreadSize(int poolSize, int queueSize, int ability, int max_pool_size) {
		max_pool_size -= poolSize;
		int size = queueSize % ability == 0 ? queueSize / ability : (queueSize / ability) + 1;
		return size < max_pool_size ? size : max_pool_size;
	}
}