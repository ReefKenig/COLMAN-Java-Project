package test;

import java.util.Random;

public class MainTrain2 {

	public static void main(String[] args) {
		
		int init=Thread.activeCount();
		
		MyGenericMessenger a1=new MyGenericMessenger(1, msg->{
			Messenger a2=Messenger.get(2);
			Messenger a3=Messenger.get(3);
			
			if(msg.startsWith("i")) {
				int x=Integer.parseInt(msg.substring(1));
				a2.addMessage(""+x*2);
			}else {
				a3.addMessage(new StringBuilder(msg).reverse().toString());
			}			
		});
		
		
		int[] sum={0};
		int[] i={1};
		
		MyGenericMessenger a2=new MyGenericMessenger(2, msg->{
			sum[0]+=Math.pow(Integer.parseInt(msg),i[0]);
			i[0]+=1;
		});
		
		StringBuffer sb=new StringBuffer();
		
		MyGenericMessenger a3=new MyGenericMessenger(3, msg->sb.append(msg));
		
		if(Thread.activeCount() - init !=3)
			System.out.println("you didn't open the right number of threads (-10) ");
		
		Random r=new Random();
		int sm=0;
		StringBuffer sb2=new StringBuffer();
		
		for(int j=1;j<=7;j++) {			
			int x=r.nextInt(100);
			sm+=Math.pow(x, j);
			sb2.append(new StringBuilder(""+x).reverse());
			a1.addMessage("i"+x);
			a1.addMessage(""+x);
		}
		
		try {Thread.sleep(100);} catch (InterruptedException e) {}
		
		if(sum[0]!=sm)
			System.out.println("error in messages a1 to a2 (-10)");
		if(!sb.toString().equals(sb2.toString()))
			System.out.println("error in messages a1 to a3 (-10)");
		
		a1.close();
		a2.close();
		a3.close();
		
		try {Thread.sleep(100);} catch (InterruptedException e) {}
		
		if(Thread.activeCount() - init !=0)
			System.out.println("you didn't close all threads (-5) ");

		System.out.println("done");
	}

}
