package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyFuture<V> implements Future<V> {
    private final Object lock = new Object(); // Used for waiting or notifying
    private V value = null; // The stored result
    private boolean isSet = false;
    private boolean isCancelled = false;

    // Callbacks saved until value is ready
    private final List<Consumer<V>> acceptCallbacks = new ArrayList<>();
    private final List<Runnable> applyCallbacks = new ArrayList<>();

    // Set the value once, throws if already done/cancelled
    public void set(V v) {
        List<Consumer<V>> acceptsToRun;
        List<Runnable> appliesToRun;
        synchronized (lock) {
            if (isSet || isCancelled) {
                throw new IllegalStateException("Future already completed or cancelled");
            }
            value = v;
            isSet = true;

            // Copy callbacks so we can run them later outside the lock
            acceptsToRun = new ArrayList<>(acceptCallbacks);
            appliesToRun = new ArrayList<>(applyCallbacks);

            // Clear original lists
            acceptCallbacks.clear();
            applyCallbacks.clear();
            lock.notifyAll(); // Wake up waiting threads
        }

        // Run all consumers
        for (Consumer<V> c : acceptsToRun) {
            try {
                c.accept(v);
            } catch (Throwable ignored) {
                // Ignore
            }
        }

        // Run all apply callbacks to forward the result for next futures
        for (Runnable r : appliesToRun) {
            try {
                r.run();
            } catch (Throwable ignored) {
                // Ignore
            }
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (isSet || isCancelled) {
                return false; // Too late
            }
            isCancelled = true;
            acceptCallbacks.clear();
            applyCallbacks.clear();
            lock.notifyAll(); // Wake waiters so they can throw CancellationException
            return true;
        }
    }

    @Override
    public boolean isCancelled() {
        synchronized (lock) {
            return isCancelled;
        }
    }

    @Override
    public boolean isDone() {
        synchronized (lock) {
            return isSet || isCancelled;
        }
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (!isSet && !isCancelled) {
                lock.wait(); // Wait until something happens
            }
            if (isCancelled) {
                throw new CancellationException();
            }
            return value;
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long millis = unit.toMillis(timeout);
        long end = System.currentTimeMillis() + millis;
        synchronized (lock) {
            while (!isSet && !isCancelled) {
                long now = System.currentTimeMillis();
                long remaining = end - now;
                if (remaining <= 0) {
                    throw new TimeoutException();
                }
                lock.wait(remaining);
            }
            if (isCancelled) {
                throw new CancellationException();
            }
            if (!isSet) {
                throw new TimeoutException(); // Just in case...
            }
            return value;
        }
    }

    // Creates a new MyFuture<R> from this one
    public <R> MyFuture<R> thenApply(Function<V, R> f) {
        final MyFuture<R> next = new MyFuture<>();

        Runnable callback = () -> {
            V v;
            synchronized (lock) {
                // If this instance was cancelled, cancel the next
                if (isCancelled) {
                    next.cancel(true); // If this got cancelled - cancel next too
                    return;
                }
                v = value;
            }
            try {
                R r = f.apply(v);
                next.set(r);
            } catch (Throwable t) {
                next.cancel(true); // If function fails, just cancel next
            }
        };

        synchronized (lock) {
            if (isCancelled) {
                // This future is cancelled. cancel next one too
                next.cancel(true);
                return next;
            }
            if (isSet) {
                // Already done, run now
            } else {
                // Register for later
                applyCallbacks.add(callback);
                return next;
            }
        }

        callback.run(); // Run immediately if already set
        return next;
    }

    // Runs consumer when value is ready
    public void thenAccept(Consumer<V> c) {
        boolean runNow = false;
        V vNow = null;
        synchronized (lock) {
            if (isCancelled) {
                return; // Do nothing if cancelled
            }
            if (isSet) {
                runNow = true;
                vNow = value;
            } else {
                acceptCallbacks.add(c);
            }
        }
        if (runNow) {
            try {
                c.accept(vNow);
            } catch (Throwable ignored) {
            }
        }
    }

}
