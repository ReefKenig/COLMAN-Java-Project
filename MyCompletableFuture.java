package test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyCompletableFuture<V> {

    private V value = null;
    private boolean isSet = false;
    private final List<Consumer<V>> acceptCallbacks = new ArrayList<>();
    private final List<Runnable> applyCallbacks = new ArrayList<>();

    // implement set
    public void set(V v) {
        if (isSet) return; // ignore if already set
        this.value = v;
        this.isSet = true;

        // Run all stored callbacks
        for (Consumer<V> c : acceptCallbacks) {
            c.accept(v);
        }
        for (Runnable r : applyCallbacks) {
            r.run();
        }
    }

    // implement thenApply
    public <R> MyCompletableFuture<R> thenApply(Function<V, R> f) {
        MyCompletableFuture<R> next = new MyCompletableFuture<>();

        Runnable callback = () -> {
            R result = f.apply(value);
            next.set(result);
        };

        if (isSet) {
            // value already available → run now
            callback.run();
        } else {
            // store for later
            applyCallbacks.add(callback);
        }
        return next;
    }

    // implement thenAccept
    public void thenAccept(Consumer<V> c) {
        if (isSet) {
            c.accept(value);
        } else {
            acceptCallbacks.add(c);
        }
    }

    // implement get
    public V get() {
        return value;
    }
}
