package test;

public class GoodCode {

    /**
     * Computes squared distance from the standard deviation
     * for each element in the input array.
     * @param array input array of integers
     * @return array of doubles where result[i] = (xi - sigma)^2
     */
	public static double[] distFromStdDev(int[] array) {
        int n = array.length;

        // Compute myu
        double sum = 0;
        for (int num : array) { sum += num; }
        double myu = sum / n;

        // Compute variance
        double sumDeviationSquared = 0;
        for (int num : array) {
            double diff = num - myu;
            sumDeviationSquared += diff * diff;
        }
        double stdDev = Math.sqrt(sumDeviationSquared / n);

        // Compute squared distance from StandardDeviation for each element
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double diff = array[i] - stdDev;
            result[i] = diff * diff;
        }

        return result;
    }
}
