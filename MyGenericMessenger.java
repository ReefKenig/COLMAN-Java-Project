package test;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyGenericMessenger extends Messenger{

    private final Consumer<String> logic;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean closed = false;
    private final Thread worker;

	public MyGenericMessenger(int id, Consumer<String> logic) {
        super(id);
        this.logic = logic;

        // Start the worker thread that keeps pulling messages
        worker = new Thread(() -> {
            try {
                while (true) {
                    // If closed and nothing left in queue, stop
                    if (closed && queue.isEmpty()) {
                        break;
                    }
                    // Try to take a message, don't block forever
                    String msg = queue.poll(50, TimeUnit.MILLISECONDS);
                    if (msg != null) {
                        logic.accept(msg); // Apply logic on the message
                    }
                }
            } catch (InterruptedException e) {
                // If interrupted, finish all messages in the queue
                String msg;
                while ((msg = queue.poll()) != null) {
                    logic.accept(msg);
                }
                Thread.currentThread().interrupt();
            }
        });
        worker.start(); // Start the thread
	}

	@Override
	void addMessage(String msg) {
        // Push message into the queue, unless it is closed
        if (!closed) {
            queue.offer(msg);
        }
	}

	@Override
	void close() {
        // Tell the worker the work is done
        closed = true;
        worker.interrupt(); // Wake it up if it was waiting
        try {
            worker.join(); // Wait until it really stops
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}

}
