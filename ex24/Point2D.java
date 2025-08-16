package test;

import java.io.Serializable;

public class Point2D implements Serializable{
	public final int x,y;
	public Point2D(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public boolean equals(Point2D p){
		return x==p.x && y==p.y;
	}
}
