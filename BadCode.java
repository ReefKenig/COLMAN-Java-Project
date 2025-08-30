package test;

import java.util.List;

public class BadCode {
	
	
	private static double dist(Point2D a, Point2D b) {
		return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
	}
	
	public static double getMaxDist(List<Point2D> ps) {
		double max=0;
		for(Point2D pi : ps) {
			for(Point2D pj : ps) {
				if(max<dist(pi,pj))
					max=dist(pi,pj);
			}
		}
		return max;
	}

}
