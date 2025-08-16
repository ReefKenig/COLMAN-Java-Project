package test;

import java.util.Random;

public class BinTree {
    int v; 
    BinTree left = null;
    BinTree right = null;
    
    private static int leafSum = 0;

    private BinTree(int v) {
        this.v = v;
    }
    
    private static Random r = new Random();

    public static BinTree generateRandomTree(int depth) {
        if (depth == 0) {
            return null;
        }
        
        int x = r.nextInt(10000);
        BinTree root = new BinTree(x);
        if (depth > 1) {
            
            boolean createLeft = r.nextBoolean();
            boolean createRight = r.nextBoolean();

            if (!createLeft && !createRight) {
                if (r.nextBoolean()) {
                    createLeft = true;
                } else {
                    createRight = true;
                }
            }

            if (createLeft) {
                root.left = generateRandomTree(depth - 1);
            }
            if (createRight) {
                root.right = generateRandomTree(depth - 1);
            }

            if (root.left == null && root.right == null) {
                leafSum += root.v;
            }
        } else {
            leafSum += root.v;
        }
        return root;
    }

    public static int getLeafSum() {
        return leafSum;
    }

    public static void resetLeafSum() {
        leafSum = 0;
    }
}