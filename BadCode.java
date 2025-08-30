package test;

import java.util.List;

public class BadCode {
	
	public static int getNum(List<Point2D> ps){
		int n=0,x=0,y=0;
		// find a pair of points with equal x,y
		for(int i=0;i<ps.size();i++)
			for(int j=i+1;j<ps.size();j++)
				if(ps.get(i).equals(ps.get(j))){
					x=ps.get(i).x;
					y=ps.get(i).y;
					break;	// no need to keep looking
				}
		
		// count how many points have the same x,y
		for(int i=0;i<ps.size();i++){
			if(ps.get(i).x==x && ps.get(i).y==y)
				n++;
		}
		return n;
	}

}
