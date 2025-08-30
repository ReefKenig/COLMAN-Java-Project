package test;

import java.util.function.Consumer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyGenericMailBox extends MailBox {

    private final Consumer<String> logic;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean closed = false;
    private final Thread worker;

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
                    // Wait for a message (interruptible)
                    msg = queue.take();
                    logic.accept(msg);
                }
            } catch (InterruptedException e) {
                // Thread can be interrupted when closing
                Thread.currentThread().interrupt();
            }
        });
        worker.start();
    }

    @Override
    void addMessage(String msg) {
        if (!closed) {
            queue.offer(msg);
        }
    }

    @Override
    void close() {
        closed = true;
        worker.interrupt(); // wake up if waiting
    }

}
