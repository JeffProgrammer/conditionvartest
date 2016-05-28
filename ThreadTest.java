package threadingtest;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ThreadTest extends Thread {
	private final static ReentrantLock lock = new ReentrantLock();
	private final static Condition conditionVariable = lock.newCondition();
	private static Queue<Integer> queue = new ArrayDeque<Integer>();
	private static boolean isDone = false;
	
	private boolean isConsumer;
	
	public ThreadTest(boolean type) {
		isConsumer = type;
	}
	
	public void run() {
		if (isConsumer)
			consumer();
		else
			producer();
	}
	
	private void consumer() {
		while (true) {
			try {
				lock.lock();
				
				while (!isDone && queue.isEmpty()) {
					conditionVariable.await();
				}
				
				// check to see if we are done
				if (isDone) {
					break;
				}
				
				System.out.println("Processed number " + queue.poll());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	private void producer() {
		for (int i = 0; i < 20; ++i) {
			lock.lock();
			queue.add(i);
			conditionVariable.signal();
			lock.unlock();
			sleepNoExcept(500);
		}
		
		// signal we are done
		isDone = true;
		lock.lock();
		conditionVariable.signal();
		lock.unlock();
	}
	
	/**
	 * Sleeps without worrying about the exception.
	 * @param milliseconds The amount of milliseconds to sleep.
	 */
	private void sleepNoExcept(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
