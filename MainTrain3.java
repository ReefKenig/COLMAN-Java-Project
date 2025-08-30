package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainTrain3 {

	
	public static void main(String[] args) {
		
		// random input
		Random r=new Random();		
		int N=100000;
		ArrayList<Integer> vs=new ArrayList<>();
		for(int i=0;i<N;i++) {
			vs.add(r.nextInt(11));
			if(r.nextBoolean()){
				int len=50+r.nextInt(50);
				int x=r.nextInt(11);
				for(int j=0;j<len;j++)
					vs.add(x);
			}
		}
		
		// time for bad code
		long bad=System.nanoTime();
		int bd=BadCode.getMaxLength(vs);
		bad=System.nanoTime()-bad;
		
		
		// time for OPT code
		long good=System.nanoTime();
		int gd=GoodCode.getMaxLength(vs);
		good=System.nanoTime()-good;
				
		
		if(gd!=bd){	// this also forces to apply the bad code
			System.out.println("your function did not get the same result (-35)");			
			System.out.println("done");
			return;
		}
		
		DecimalFormat f = new DecimalFormat("#,###.##");
		System.out.println("~bad time:\t"+f.format(bad));
		System.out.println("~your time:\t"+f.format(good));
		double optRate=(double)bad/good;
		System.out.println("~opt rate: "+f.format(optRate));

		if(optRate<=1)
			optRate=0;			
		
		if(optRate<5)
			System.out.println("you can do better optimizations (-"+(35-Math.round(35*(optRate)/5.0))+")");
		System.out.println("done");
		
	}


}


