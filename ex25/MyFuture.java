package test;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyFuture<V> implements Future<V>{

    public void set(V value) {
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
		
    }

    @Override
    public boolean isCancelled() {
		return false;
    }

    @Override
    public boolean isDone() {
		return false;	
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
		return null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
    }


    public <R> MyFuture<R> thenApply(Function<V,R> f){
		return null;
    }

    public void thenAccept(Consumer<V> c){        
    }

}
