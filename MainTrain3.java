package test;

import java.text.DecimalFormat;
import java.util.Random;

public class MainTrain3 {


    private static boolean equals(double[] a, double[] b) {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++)
        	if(a[i]!=b[i])
                return false;
        return true;
    }

    public static void main(String[] args) {
        // Random input
        Random r = new Random();
        int input[] = new int[1000];
        for (int i = 0; i < input.length; i++)
            input[i] = -1000 + r.nextInt(10001);

        // Time for bad code
        long bad = System.nanoTime();
        double[] db = BadCode.distFromStdDev(input);
        bad = System.nanoTime() - bad;

        // Time for efficient code
        long good = System.nanoTime();
        double[] dg = GoodCode.distFromStdDev(input);
        good = System.nanoTime() - good;

        // Comparing outputs
        if (!equals(dg, db)) {
            System.out.println("Your function did not get the same result (-35)");
            System.out.println("Done");
            return;
        }

        // Formatting and displaying results
        DecimalFormat f = new DecimalFormat("#,###");
        System.out.println("Bad time:\t" + f.format(bad));
        System.out.println("Your time:\t" + f.format(good));
        double optRate =  bad / good;
        System.out.println("Optimization rate: " + optRate);
        if (optRate < 200)
            System.out.println("You can do better optimizations (-" + (Math.round(35 * (200 - optRate) / 200)) + ")");
        System.out.println("Done");
    }


}
