package test;

import java.util.List;

public class GoodCode {

	public static int getMaxLength(List<Integer> vs){
		if (vs.isEmpty()) {
            return 0; // Array is empty
        }

        int max = 1; // Longest run so far
        int currentLen = 1; // Length of the current run

        for (int i = 1; i < vs.size(); i++) {
            // Check if same as previous number
            if (vs.get(i).equals(vs.get(i - 1))) {
                currentLen++; // Increment count
            } else {
                currentLen = 1; // Reset streak
            }
            // Update max if needed
            if (currentLen > max) {
                max = currentLen;
            }
        }

        return max;
	}
}
