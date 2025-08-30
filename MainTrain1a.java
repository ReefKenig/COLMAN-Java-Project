package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MainTrain1a {
	
	public static void delay(long time){
		try {Thread.sleep(time);} catch (InterruptedException e) {}
	}

	   public static void main(String[] args) {
	        Q1a q = new Q1a();

	        int tc = Thread.activeCount();
	        double x = Math.random() * 10;
	        Future<Double> f = q.threadIt(() -> { delay(1000); return x; });

	        if (Thread.activeCount() - tc != 1)
	            System.out.println("threadIt should open only one thread (-5)");

	        try {
	            if (f.get().doubleValue() != x)
	                System.out.println("the Future did not return the correct value (-5)");
	        } catch (Exception e) {
	            System.out.println("problem with the Future object (-5)");
	        }

	        List<Future<Double>> futureList = new ArrayList<>();
	        for (int i = 0; i < 3; i++) {
	            double y = x + i;
	            futureList.add(q.threadIt(() -> { delay(1000); return y; }));
	        }

	        List<Double> results = q.waitAllFutures(futureList);
	        if(results.size() != 3)
	        {
	        	System.out.println("the results did not return the correct size (-15)");
	        }
	        else
	        {
		        for (int i = 0; i < results.size(); i++) {
		            if (results.get(i) == null || results.get(i) != x + i) {
		                System.out.println("Problem with waitAllFutures or Future results (-5)");
		                break;
		            }
		        }
	        }

	        q.close();
	        System.out.println("done");
	    }

}
