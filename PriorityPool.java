package test;

public class PriorityPool {
    private final Worker[] workers;

    public PriorityPool(int workerThreads, int capacity) {
        if (workerThreads <= 0) throw new IllegalArgumentException("WorkerThreads must be > 0");
        workers = new Worker[workerThreads];
        for (int i = 0; i < workerThreads; i++) {
            int pr = i + 1; // Priority increases with index
            workers[i] = new Worker(capacity, pr);
            workers[i].start(); // Pool should be ready immediately
        }
    }

    public int addTask(Runnable task, int priority) {
        if (priority <= 0) priority = 1;
        if (priority > workers.length) priority = workers.length;

        for (int p = priority; p >= 1; p--) {
            Worker w = workers[p - 1];
            if (w.add(task)) {
                return p;
            }
        }
        return 0;
    }

    public void stopAll() {
        for (Worker w : workers) {
            w.close();
        }
    }
}
