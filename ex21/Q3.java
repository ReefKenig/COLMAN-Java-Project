package test;

import java.util.List;

public class Q3 {

    public static int goodCalc(List<Integer> arr){
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
}
