package test;

public class BadCode {

	// Calculate the average of array elements
    private static double average(int[] array) {
        double sum = 0;
        for (int value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    // Calculate standard deviation
    private static double standardDeviation(int[] array) {
        double sumDeviationSquared = 0;
        for (int value : array) {
            sumDeviationSquared += Math.pow((value - average(array)), 2);
        }
        return Math.sqrt(sumDeviationSquared / array.length);
    }

    // Calculate squared distance from the standard deviation
    public static double[] distFromStdDev(int[] array) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Math.pow(array[i] - standardDeviation(array), 2);
        }
        return result;
    }
}
