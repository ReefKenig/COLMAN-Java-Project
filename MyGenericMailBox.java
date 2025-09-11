package test;

import java.util.function.Consumer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyGenericMailBox extends MailBox {

    private final Consumer<String> logic; // What to do with each message
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean closed = false; // Signal for shutdown
    private final Thread worker; // Background worker

    public MyGenericMailBox(int id, Consumer<String> logic) {
        super(id);
        this.logic = logic;

        // Start worker thread
        worker = new Thread(() -> {
            try {
                while (true) {
                    String msg;
                    // If closed and no more messages, exit
                    if (closed && queue.isEmpty()) {
                        break;
                    }
                    msg = queue.take(); // Wait for next message
                    logic.accept(msg); // Handle message
                }
            } catch (InterruptedException e) {
                // If interrupted, exit the thread
                Thread.currentThread().interrupt();
            }
        });
        worker.start();
    }

    @Override
    void addMessage(String msg) {
        if (!closed) {
            queue.offer(msg); // Enqueue the message
        }
    }

    @Override
    void close() {
        closed = true;
        worker.interrupt(); // wake up if waiting
    }

}
