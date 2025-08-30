package test;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class MainTrain2 {

    public static void test(int size){
        int[] data=new int[size];
        Random r=new Random();
        int mx=0;
        for(int i=0;i<data.length;i++){
            data[i]=r.nextInt(1000000);
            if(data[i]>mx)
                mx=data[i];
        }
        Q2 q=new Q2(data);
        ForkJoinPool pool=new ForkJoinPool();
        boolean[] stop={false};
        long[] count={0};
        new Thread(()->{
            while(!stop[0]){
                long x=pool.getQueuedTaskCount();
                if(count[0]<x)
                    count[0]=x;
            }
        }).start();  
        int max = pool.invoke(q);
        pool.shutdown();
        stop[0]=true;
        
        if(max!=mx){
            System.out.println("wrong result for Q2 (-5)");
        }
        long ps=(int)(Math.log(size)/Math.log(2));
        if(count[0]<ps)
            System.out.println("wrong number of threads opend (-5)");
        
        pool.shutdownNow();
    }
    public static void main(String[] args) {
        try{test(512);}catch(Exception e){
            System.out.println("your code threw an exception (-10)");
        }
        try{test(1024);}catch(Exception e){
            System.out.println("your code threw an exception (-10)");
        }
        try{test(2048);}catch(Exception e){
            System.out.println("your code threw an exception (-10)");
        }
        System.out.println("done");
    }
        
}