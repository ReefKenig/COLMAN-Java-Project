package test;

import java.util.concurrent.*;

public class Q1a {
	
	ExecutorService es;
	
	public Q1a() {
		es = Executors.newSingleThreadExecutor();
	}

	public void close(){
		es.shutdown();
	}
	
	
	// implement threadIt() and waitAllFutures() methods here
	
}

