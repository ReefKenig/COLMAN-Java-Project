package test;

import java.util.List;

public class BadCode {
	
	public static int getMaxLength(List<Integer> vs){
		int max=0;
		for(int i=0;i<vs.size();i++){ // for each value x
			int x=vs.get(i);
			int len=1;
			int y;
			// find the length of next values that are equal to x
			for(int j=i+1; j<vs.size() && (y=vs.get(j))==x; j++,len++);
			if (len>max) // maintain max length
				max=len;
		}
		return max;
	}
}
