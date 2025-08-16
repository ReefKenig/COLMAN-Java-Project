package test;


public class MainTrain1 {
	
	public static class Task implements Runnable{
		private boolean done=false;
		long wait;
		public Task(long wait){
			this.wait=wait;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {}
			done=true;
		}

		public boolean get(){
			return done;
		}
	}

	public static void testSingleWorker(){

		// test a single Worker
		int initial = Thread.activeCount();
		int total=0;
		try{
			Worker w=new Worker(1, 5);
			w.start();
			Task t=new Task(100);
			boolean b1 = w.add(t);	// true
			boolean b2 = w.add(new Task(100)); // false
			if(!b1 || b2){
				System.out.println("problem with Worker add return value (-5)");				
				total+=5;
			}
			
			Thread.sleep(200);
			w.close();
			Thread.sleep(100);
			if (!t.done || Thread.activeCount()!=initial){
				System.out.println("problem with executing or closing Worker thread (-5)");
				total+=5;
			}
		Thread.sleep(100);

		}catch(Exception e){
			System.out.println("Worker caused an exception (-"+(10-total)+")");
		}
	}

	public static void testPriorityPool1(){
		int total=0;
		try{
			// test a Priority Pool with a single Worker with a capcity of 5 tasks
			int initial = Thread.activeCount();
			PriorityPool pp=new PriorityPool(1, 5);
			Task[] tasks=new Task[10];
			int[] ps=new int[10];
			for(int i=0;i<tasks.length;i++){
				tasks[i]=new Task(100);
				ps[i]=pp.addTask(tasks[i], 1);
			}
			int sum1=0,sum0=0;
			for(int i=0;i<5;sum1+=ps[i],i++);
			for(int i=5;i<10;sum0+=ps[i],i++);
			if(sum1!=5 || sum0!=0){
				System.out.println("problem with addTask and a single Worker (-5)");
				total+=5;
			}

			Thread.sleep(600);	// wait for all 5 tasks to end
			pp.stopAll();
			Thread.sleep(100);
			boolean b=true,b0=false;
			for(int i=0;i<5;b&=tasks[i].done,i++);
			for(int i=5;i<10;b0|=tasks[i].done,i++);
			if(!b || b0){
				System.out.println("problem with executing a single Worker in a PriorityPool (-5)");
				total+=5;
			}
			if (Thread.activeCount()!=initial){
				System.out.println("problem with closing Worker thread in a PriorityPool (-5)");
				total+=5;
			}

			Thread.sleep(100);


		}catch(Exception e){
			System.out.println("PriorityPool caused an exception (-"+(15-total)+")");
		}
	}


	public static void testPriorityPool2(){
		int total=0;
		try{
			// test a Priority Pool with 3 Workers with a capcity of 7 tasks
			int initial = Thread.activeCount();
			PriorityPool pp=new PriorityPool(3, 7);
			Task[] tasks=new Task[28];
			int[] ps=new int[28];
			for(int i=0;i<tasks.length;i++){
				tasks[i]=new Task(1000); // 1 secs
				ps[i]=pp.addTask(tasks[i], 3);
			}
			// stop all before completion
			pp.stopAll();
			int sum3=0,sum2=0,sum1=0,sum0=0;
			for(int i=0;i<7;sum3+=ps[i],i++);
			for(int i=7;i<14;sum2+=ps[i],i++);
			for(int i=14;i<21;sum1+=ps[i],i++);
			for(int i=21;i<28;sum0+=ps[i],i++);
			if(sum3!=3*7 || sum2!=2*7 || sum1!=7 || sum0!=0){
				System.out.println("problem with addTask and a multiple Workers (-5)");
				total+=5;
			}

			Thread.sleep(100);
			int c=0;
			for(int i=0;i<28;i++){
				if(tasks[i].done)
					c++;
			}
			if(c>3){
				System.out.println("problem with executing multiple Workers in a PriorityPool (-5)");
				total+=5;
			}
			if (Thread.activeCount()!=initial){
				System.out.println("problem with closing Worker thread in a PriorityPool with multiple workers (-5)");
				total+=5;
			}

		
		}catch(Exception e){
			System.out.println("PriorityPool caused an exception (-"+(15-total)+")");
		}
	}

	public static void main(String[] args) {
		
		testSingleWorker();
		testPriorityPool1();
		testPriorityPool2();

		System.out.println("done");		
	}

}
