package test;

import java.util.concurrent.RecursiveTask;

public class SumLeaves extends RecursiveTask<Integer> {
    private final BinTree tree;

    public SumLeaves(BinTree tree) {
        this.tree = tree;
    }

    @Override
    protected Integer compute() {
        if (tree == null) { return 0; }

        // Only one leaf and no children
        if (tree.left == null && tree.right == null) { return tree.v; }

        int rightSum = 0;
        SumLeaves leftT = null;

        // Fork left subtree
        if (tree.left != null) {
            leftT = new SumLeaves(tree.left);
            leftT.fork();
        }

        // Compute right subtree synchronously
        if (tree.right != null) {
            SumLeaves rightT = new SumLeaves(tree.right);
            rightSum = rightT.compute();
        }

        // Join left result
        int leftSum = 0;
        if (leftT != null) { leftSum = leftT.join(); }

        return leftSum + rightSum;
    }
}
