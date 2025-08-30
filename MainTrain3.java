package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainTrain3 {

	
	public static void main(String[] args) {
		
		// random input
		Random r=new Random();
		List<Point2D> ps=new ArrayList<>();
		for(int i=0;i<10000;i++) {
			ps.add(new Point2D(-1000+r.nextInt(2001),-1000+r.nextInt(2001)));
		}
		
		// time for bad code
		long bad=System.nanoTime();
		double bd=BadCode.getMaxDist(ps);				
		bad=System.nanoTime()-bad;
		
		
		// time for OPT code
		long good=System.nanoTime();
		double gd=GoodCode.getMaxDist(ps);
		good=System.nanoTime()-good;
				
		
		if(Math.abs(gd-bd)>0.001){
			System.out.println("your function did not get the same result (-35)");
			System.out.println("done");
			return;
		}
		
		DecimalFormat f = new DecimalFormat("#,###.##");
		System.out.println("bad time:\t"+f.format(bad));
		System.out.println("your time:\t"+f.format(good));
		double optRate=(double)bad/good;
		System.out.println("opt rate: "+f.format(optRate));

		if(optRate<=1)
			optRate=0;			
		
		if(optRate<5)
			System.out.println("you can do better optimizations (-"+(35-Math.round(35*(optRate)/5.0))+")");
		System.out.println("done");
		
	}


}


