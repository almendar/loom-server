package pl.tomaszkogut;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Database implements AutoCloseable {

    private static ScheduledExecutorService es;

    private static Map<Long, User> data;
    static {
        data = Map.of(
                1L, new User(1L, "Joe"),
                2L, new User(2L, "Jimmy"),
                3L, new User(3L, "Mary")
        );
        es  = Executors.newScheduledThreadPool(4);
    }

    public Optional<User> get(long id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(data.get(id));
    }

    public CompletableFuture<Optional<User>> getAsync(long id) {
        var cf = new CompletableFuture<Optional<User>>();
//        var  dEs = CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS, es);
        es.schedule(() ->
        {
            System.out.println("Compliting");
            cf.complete( Optional.ofNullable(data.get(id)));},5000, TimeUnit.MILLISECONDS);
        return cf;
    }

    @Override
    public void close() throws Exception {
        es.close();
    }
}
