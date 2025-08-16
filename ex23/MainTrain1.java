package test;

import java.util.Random;

public class MainTrain1 {
	

	public static void main(String[] args) {
		
		Random r=new Random();
		
		// test a
		try{
			String[] rslt={""};
			
			MyCompletableFuture<String> f=new MyCompletableFuture<>();
			f.thenAccept(s->rslt[0]=new StringBuilder(s).reverse().toString());
			
			String s=""+r.nextInt(100000);
			String rv=new StringBuilder(s).reverse().toString();

			f.set(s);

			if(!rslt[0].equals(rv))
				System.out.println("wrong result for test a (-10)");

		}catch(Exception e){
			System.out.println("you got an exception for test a (-10)");
		}


		// test b
		try{
			MyCompletableFuture<String> fs=new MyCompletableFuture<>();
			MyCompletableFuture<Integer> fi=fs.thenApply(s->s.hashCode());
			
			String s=""+r.nextInt(100000);
			int rslt=s.hashCode();

			fs.set(s);

			if(rslt!=fi.get())
				System.out.println("wrong result for test b (-10)");

		}catch(Exception e){
			System.out.println("you got an exception for test b (-10)");
		}

		// test c
		try{
			Boolean[] bs={null};

			MyCompletableFuture<String> fs=new MyCompletableFuture<>();
			MyCompletableFuture<Integer> fi=fs.thenApply(s->s.hashCode());
			MyCompletableFuture<Boolean> fb=fi.thenApply(i->i%2==0);
			fb.thenAccept(b->bs[0]=b);
			
			String s=""+r.nextInt(100000);
			int rslt=s.hashCode();
			boolean b=rslt%2==0;

			fs.set(s);

			if(bs[0]!=null && b!=bs[0].booleanValue())
				System.out.println("wrong result for test c (-10)");

		}catch(Exception e){
			System.out.println("you got an exception for test c (-10)");
		}

		System.out.println("done");		
	}

}
