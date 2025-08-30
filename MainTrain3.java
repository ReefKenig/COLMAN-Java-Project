package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainTrain3 {

	
	public static void main(String[] args) {
		
		// random input
		Random r=new Random();		
		Set<Point2D> pst=new HashSet<>();
		int N=10000;
		for(int i=0;i<N;i++) {
			Point2D pi=new Point2D(-1000+r.nextInt(2001),-1000+r.nextInt(2001));			
			pst.add(pi);
		}
		

		List<Point2D> ps=new ArrayList<>();
		ps.addAll(pst);

		int eq=100+r.nextInt(101);
		int x=-1000+r.nextInt(2001);
		int y=-1000+r.nextInt(2001);

		Point2D p=new Point2D(x,y);
		ps.removeIf(pi->pi.equals(p));
		for(int i=0;i<eq;i++){
			ps.add(r.nextInt(ps.size()), p);
		}


		
		// time for bad code
		long bad=System.nanoTime();
		int bd=BadCode.getNum(ps);				
		bad=System.nanoTime()-bad;
		
		
		// time for OPT code
		long good=System.nanoTime();
		int gd=GoodCode.getNum(ps);
		good=System.nanoTime()-good;
				
		
		if(gd!=bd || gd!=eq){	// this also forces to apply the bad code
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


