package test;

import java.util.concurrent.RecursiveTask;

public class Q2 extends RecursiveTask<Integer> {
    private final int[] data;
    private final int start;
    private final int end;

    // Public constructor starts with the whole array
    public Q2(int[] data){
        this(data, 0, data.length);
    }

    // Private constructor for divided array
    private Q2(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int length = end - start;

        // Base case: one element
        if (length == 1) {
            return data[start];
        }

        // Split in 2 halves
        int mid = start + length / 2;

        // Fork right half
        Q2 rightTask = new Q2(data, mid, end);
        rightTask.fork();

        // Compute left half in current thread
        Q2 leftTask = new Q2(data, start, mid);
        int leftMax = leftTask.compute();

        // Join right result
        int rightMax = rightTask.join();

        // Combine and find max value
        return Math.max(leftMax, rightMax);
    }
}
