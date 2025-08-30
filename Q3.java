package test;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Q3 {

    private static final Random rand = new Random();

    public static int goodCalc(List<Integer> arr){
        int n = arr.size();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = arr.get(i);
        }

        // Find median index
        int k = n / 2;
        return quickSelect(nums, 0, n - 1, k);
    }

    // Quickselect algorithm, narrows down the array until pivot = k
    private static int quickSelect(int[] nums, int left, int right, int k) {
        while (left <= right) {
            int pivot = partition(nums, left, right);
            if (pivot == k) {
                return nums[pivot];
            } else if (pivot < k) {
                left = pivot + 1;
            } else {
                right =pivot - 1;
            }
        }
        throw new RuntimeException("Should not reach here");
    }

    // Partition like in quicksort: pivot ends up in its final sorted spot
    private static int partition(int[] nums, int left, int right) {
        int pivotIndex = left + rand.nextInt(right - left + 1);
        int pivotValue = nums[pivotIndex];
        // Move pivot to end
        swap(nums, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }
        // Put pivot in its place
        swap(nums, storeIndex, right);
        return storeIndex;
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
