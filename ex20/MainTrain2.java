package test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainTrain2 {
    private static Random r = new Random();

    public static void main(String[] args) {
        int randomDepth = 5 + r.nextInt(20);

        BinTree.resetLeafSum(); 
        BinTree tree = BinTree.generateRandomTree(randomDepth);

        SumLeaves sumLeavesTask = new SumLeaves(tree);
        ForkJoinPool fjp = new ForkJoinPool();
        Future<Integer> leafSum = fjp.submit(sumLeavesTask);

        fjp.shutdown();
        boolean ok = true;
        try {
            ok = fjp.awaitTermination(1, TimeUnit.SECONDS);
            if (!ok) {
                System.out.println("your program is stuck (-30)");
                fjp.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("you got an exception while waiting for termination (-30)");
        }

        if (ok) {
            try {
                int expectedSum = BinTree.getLeafSum();
                if (leafSum.get() != expectedSum) {
                    System.out.println("you didn't return the correct leaf sum (-20)");
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("exception in future get (-20)");
            }

            if (fjp.getStealCount() <= 1) {
                System.out.println("you didn't use the threads correctly (-10)");
            }
        }

        System.out.println("done");
    }
}



