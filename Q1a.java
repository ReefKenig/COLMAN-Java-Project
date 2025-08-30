package test;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Q1a {
	
	ExecutorService es;
	
	public Q1a() {
		es = Executors.newSingleThreadExecutor();
	}

	public void close(){
		es.shutdown();
	}
	
	
	// implement threadIt() and waitAllFutures() methods here

    /**
     * Runs the given Supplier in a separate thread (via the executor)
     * and returns a Future that represents its result.
     * @param f a Supplier that produces a value
     * @return a Future<V> that will hold the computed value
     */
    public <V> Future<V> threadIt(Supplier<V> f) {
        // turns the Supplier object into a callable
        return es.submit(f::get);
    }

    /**
     * Waits for all futures to complete and collects their results
     * in the same order as the input list.
     * If a future fails or is interrupted - return null
     * @param futures list of Future<V> tasks
     * @return list of results
     */
	public <V> List<V> waitAllFutures(List<Future<V>> futures) {
        List<V> results = new ArrayList<>(futures.size());
        for (Future<V> future: futures) {
            try {
                results.add(future.get()); // Blocks until result is available
            } catch (InterruptedException e) {
                // If task was interrupted - return null
                Thread.currentThread().interrupt();
                results.add(null);
            } catch(ExecutionException e) {
                // If task failed - return null
                results.add(null);
            }
        }
        return results;
    }
}

