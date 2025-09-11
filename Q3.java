package test;

import java.util.List;
import java.util.Random;


public class Q3 {

    private static final Random rand = new Random();

    public static int goodCalc(List<Integer> arr) {
        int n = arr.size();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = arr.get(i);
        }

        // The median is the element at index n/2 in sorted order
        int k = n / 2;
        return quickSelect(nums, 0, n - 1, k);
    }

    // Quickselect: repeatedly partition until pivot = k
    private static int quickSelect(int[] nums, int left, int right, int k) {
        while (left <= right) {
            int pivotIndex = partition(nums, left, right);
            if (pivotIndex == k) {
                return nums[pivotIndex];
            } else if (pivotIndex < k) {
                left = pivotIndex + 1;
            } else {
                right = pivotIndex - 1;
            }
        }
        throw new RuntimeException("Quickselect failed");
    }

    // Partition like in quicksort: pivot ends up in its final sorted position
    private static int partition(int[] nums, int left, int right) {
        int pivotIndex = left + rand.nextInt(right - left + 1);
        int pivotValue = nums[pivotIndex];
        swap(nums, pivotIndex, right); // move pivot to end
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }
        swap(nums, storeIndex, right); // move pivot back to final place
        return storeIndex;
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}