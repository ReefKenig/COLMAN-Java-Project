package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;


public class PooledThread{

    private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
    private final Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public PooledThread() {
        // Worker thread loops until idle for 1 second
        worker = new Thread(() -> {
            try {
                while (running.get()) {
                    // Wait up to 1 second for a task
                    Runnable task = queue.poll(1, TimeUnit.SECONDS);
                    if (task == null) {
                        // exit loop
                        running.set(false);
                        break;
                    }
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        worker.start();
    }

    private static class MyFuture<V> implements Future<V> {
        private V value;
        private boolean done = false;
        private Exception exception = null;

        public synchronized void set(V v) {
            value = v;
            done = true;
            notifyAll();
        }

        public synchronized void setException(Exception e) {
            exception = e;
            done = true;
            notifyAll();
        }

        @Override
        public synchronized V get() throws InterruptedException, ExecutionException {
            while (!done) {
                wait();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return value;
        }

        @Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }
        @Override public boolean isCancelled() { return false; }
        @Override public synchronized boolean isDone() { return done; }
        @Override public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            long ms = unit.toMillis(timeout);
            long end = System.currentTimeMillis() + ms;
            while (!done && System.currentTimeMillis() < end) {
                wait(ms);
            }
            if (!done) throw new TimeoutException();
            if (exception != null) throw new ExecutionException(exception);
            return value;
        }
    }

	public <V> Future<V> add(Callable<V> task)  {
		MyFuture<V> future = new MyFuture<>();
        Runnable wrapped = () -> {
            try {
                V result = task.call();
                future.set(result);
            } catch (Exception e) {
                future.setException(e);
            }
        };
        queue.offer(wrapped);
        return future;
	}
}
