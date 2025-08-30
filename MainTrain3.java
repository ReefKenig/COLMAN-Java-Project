package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainTrain3 {

    
    public static int badCalc(List<Integer> arr){
        int chosen=0;
        for(int i=0;i<arr.size();i++){
            int val=arr.get(i);
            int countBigger=0;
            int countSmaller=0;
            for(int j=0;j<arr.size();j++){
                if(i!=j){
                    if(val>arr.get(j)) countBigger++;
                    if(val<arr.get(j)) countSmaller++;
                }
            }
            if(countBigger==countSmaller)
                chosen=val;
        }        
        return chosen;
    }

    


    public static void main(String[] args) {
        Random r=new Random();
        ArrayList<Integer> arr=new ArrayList<>();
        for(int i=0;i<10001;i++)
            arr.add(1+r.nextInt(10001));
        List<Integer> uarr=arr.stream().distinct().collect(Collectors.toList());
        if(uarr.size()%2==0)
            uarr.add(2000);

        long t=System.nanoTime();
        int midB=badCalc(uarr);
        long badTime=System.nanoTime()-t;

        System.out.println("badTime: "+badTime);

        t=System.nanoTime();
        int midG=Q3.goodCalc(uarr);
        long goodTime=System.nanoTime()-t;

        if (midG!=midB){
            System.out.println("you didn't get the same result. (-35)");
            System.out.println("done");
            return;
        }
        double ratio=(double)badTime/(double)goodTime;
        System.out.println("goodTime: "+goodTime);
        System.out.println("ratio: "+ratio);
    
        if(ratio<20){
            int p=35-(int)(ratio/20*35);
            System.out.println("you can write a faster code (-"+p+")");
        }
        
        System.out.println("done");
    }
}
