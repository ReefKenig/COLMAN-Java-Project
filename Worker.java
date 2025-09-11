package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {
    private final int capacity;
    private final int priority;
    private final BlockingQueue<Runnable> queue;
    private final AtomicInteger occupied = new AtomicInteger(0);

    private volatile boolean closed = false;
    private volatile boolean started = false;
    private Thread thread;

    public Worker(int capacity, int priority) {
        this.capacity = capacity;
        this.priority = priority;
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    // add any other helping methods as needed
    public synchronized void start() {
        if (started) return;
        started = true;

        thread = new Thread(() -> {
            try {
                while (true) {
                    // If we were told to close before taking more tasks, break
                    if (closed) break;

                    Runnable task;
                    try {
                        task = queue.take(); // Wait for next task
                    } catch (InterruptedException e) {
                        // If interrupted and closed, exit. Otherwise continue
                        if (closed) break;
                        else continue;
                    }

                    // Execute the task. Decrement only after run finishes
                    try {
                        task.run();
                    } catch (Throwable t) {
                        // Don't kill the worker and ignore errors
                    } finally {
                        occupied.decrementAndGet();
                    }
                }
            } finally {
                // Thread exiting
            }
        });

        // Apply the requested priority
        try {
            thread.setPriority(priority);
        } catch (SecurityException ignored) { /* Ignore if not allowed */ }
        thread.start();
    }

    public boolean add(Runnable task) {
        if (closed) return false;

        // Reserve a slot atomically
        while (true) {
            int occ = occupied.get();
            if (occ >= capacity) {
                return false; // No free slot
            }
            if (occupied.compareAndSet(occ, occ + 1)) {
                break; // Reserved
            }
        }

        // Enqueue the task. If enqueueing fails - rollback the reservation
        boolean offered = queue.offer(task);
        if (!offered) {
            occupied.decrementAndGet();
            return false;
        }
        return true;
    }

    public void close() {
        closed = true;
        if (thread != null) thread.interrupt();
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
