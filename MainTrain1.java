package test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainTrain1 {

	public static void main(String[] args) {
		int count=Thread.activeCount();
		PooledThread pt=new PooledThread();
		if(Thread.activeCount()!= count +1 ){
			System.out.println("your PooledThread object did not open a new thread (-35)");
			System.out.println("done");
			return;
		}
		
		int vals[] = new int[15];
		ArrayList<Future<Integer>> arr=new ArrayList<>();
		Random r=new Random();		
		
		for(int i=0;i<15;i++){
			int x=r.nextInt(100);
			arr.add(pt.add(()->x));
			vals[i]=x;
		}
		
		// wait for 1.5 second
		try { Thread.sleep(1500);} catch (InterruptedException e) {}
		// by now your pooled thread should close

		if(Thread.activeCount()!= count )
			System.out.println("your PooledThread object did not close its thread (-15)");

		int i=0;
		for(Future<Integer> f : arr){
			try {
				if(f.get()!=vals[i]){
					System.out.println("wrong value returned in Future object (-1)");
				}
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("Exception: wrong value returned in Future object (-1)");
			}
			i++;
		}
		
		System.out.println("done");
	}

}
