package test;


import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;

public class MainTrain1 {
    private static int totalPoints = 40;
	static Random r=new Random();

    public static void main(String[] args) {
        testSetAndGet(); // 5
        testCancel(); //15
        testTimeout(); //5
        testThenApply(); //10
        testThenAccept(); //5
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
        System.out.println("done");
    }

    private static void deductPoints(int points, String message) {
        System.out.println(message + " (-" + points + ")");
        totalPoints -= points;
    }

    private static void testSetAndGet() {
        try {
			int x=r.nextInt(1000);
            MyFuture<Integer> future = new MyFuture<>();
            Thread t = new Thread(() -> {
                try {
                    if (future.get() != x) {
                        deductPoints(5, "testSetAndGet failed: Expected "+x);
                    }
                } catch (Exception e) {
                    deductPoints(5, "testSetAndGet threw an exception: " + e.getMessage());
                }
            });
            t.start();
            Thread.sleep(100);
            future.set(x);
            t.join(1000);
            if (t.isAlive()) {
                t.interrupt();
                deductPoints(5, "testSetAndGet failed: get() did not return");
            }
        } catch (Exception e) {
            deductPoints(5, "testSetAndGet threw an exception: " + e.getMessage());
        }
    }

    private static void testCancel() {
		int[] remaining={15};
        try {
            MyFuture<String> future = new MyFuture<>();
            if (!future.cancel(true)) {
                deductPoints(5, "testCancel failed: Expected successful cancel");				
				remaining[0]-=5;
            }
            
			if (!future.isCancelled()) {
                deductPoints(5, "testCancel failed: Expected isCancelled to be true");				
				remaining[0]-=5;
            }
            
			Thread t = new Thread(() -> {
                try {
                    future.get();
                    deductPoints(5, "testCancel failed: Expected get() to throw CancellationException");
					remaining[0]-=5;
                } catch (CancellationException ignored) {					
                } catch (Exception e) {
					deductPoints(5, "testCancel failed: get() did not return");
					remaining[0]-=5;
                }
            });
            t.start();
            t.join(1000);
            if (t.isAlive()) {
                t.interrupt();
            }
        } catch (Exception e) {
            deductPoints(remaining[0], "testCancel threw an exception: " + e.getMessage());
        }
    }

    private static void testTimeout() {
        try {
            MyFuture<Integer> future = new MyFuture<>();
            Thread t = new Thread(() -> {
                try {
                    future.get(1, TimeUnit.SECONDS);
                    deductPoints(5, "testTimeout failed: Expected TimeoutException");
                } catch (TimeoutException ignored) {
                } catch (Exception e) {
                    deductPoints(5, "testTimeout failed: get() did not return");
                }
            });
            t.start();
            t.join(1500);
            if (t.isAlive()) {
                t.interrupt();                
            }
        } catch (Exception e) {
            deductPoints(5, "testTimeout threw an exception: " + e.getMessage());
        }
    }

	private static void testThenApply() {
        try {
            Random r = new Random();
            int x = r.nextInt(1000);
            MyFuture<Integer> future = new MyFuture<>();
            MyFuture<String> nextFuture = future.thenApply(i -> "Value: " + i);
            Thread t1 = new Thread(() -> future.set(x));
            t1.start();
            t1.join(1000);
            Thread t2 = new Thread(() -> {
                try {
                    if (!nextFuture.get().equals("Value: " + x)) {
                        deductPoints(10, "testThenApply failed: Expected 'Value: " + x + "'");
                    }
                } catch (Exception e) {
                    deductPoints(10, "testThenApply failed unknown error");
                }
            });
            t2.start();
            t2.join(1000);
            if (t2.isAlive()) {
                t2.interrupt();                
            }
        } catch (Exception e) {
            deductPoints(5, "testThenApply threw an exception: " + e.getMessage());
        }
    }

    private static void testThenAccept() {
        try {
            Random r = new Random();
            int x = r.nextInt(1000);
            MyFuture<Integer> future = new MyFuture<>();
            final boolean[] accepted = {false};
            future.thenAccept(value -> accepted[0] = (value == x));
            Thread t1 = new Thread(() -> future.set(x));
            t1.start();
            t1.join(1000);
            Thread t2 = new Thread(() -> {
                if (!accepted[0]) {
                    deductPoints(5, "testThenAccept failed: Consumer did not receive expected value");
                }
            });
            t2.start();
            t2.join(1000);
            if (t2.isAlive()) {
                t2.interrupt();
                
            }
        } catch (Exception e) {
            deductPoints(5, "testThenAccept threw an exception: " + e.getMessage());
        }
    }
}
