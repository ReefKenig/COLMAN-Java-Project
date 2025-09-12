package test;

import java.util.concurrent.RecursiveTask;

public class Q2 extends RecursiveTask<Integer> {
    private final int[] data;
    private final int start;
    private final int end;

    public Q2(int[] data) {
        this(data, 0, data.length);
    }

    // Constructor for sub-arrays
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
            return (data[start] % 2 != 0) ? 1 : 0;
        }

        // Recursive case: split in half
        int mid = start + length / 2;

        Q2 left = new Q2(data, start, mid);
        Q2 right = new Q2(data, mid, end);

        // Fork both halves
        left.fork();
        right.fork();

        // Join results
        int leftResult = left.join();
        int rightResult = right.join();

        return leftResult + rightResult;
    }
}
