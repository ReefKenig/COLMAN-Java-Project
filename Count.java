package test;

import java.util.concurrent.atomic.AtomicInteger;

public class Count {
	private final AtomicInteger count;

	public Count() { 
		count = new AtomicInteger(0);
	}

    /**
     * Increments the counter atomically.
     * Multiple threads can call this safely.
     */
	public  void inc() {
		count.incrementAndGet();
	}

    /**
     * @return the current value of the counter (thread-safe)
     */
	public int get() {
		return count.get();
	}
}
