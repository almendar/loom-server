package pl.tomaszkogut;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiThreadsServer extends Server {

    private Executor es;

    public MultiThreadsServer(Database db) {
        super(db);
        es = Executors.newCachedThreadPool();
    }

    @Override
    void handleConnection(Connection c) {
        Runnable r = () -> {
            var maybeUser = db.get(c.req().id());
            maybeUser.ifPresentOrElse(
                    user -> c.write(new Ok(user)),
                    () -> c.write(new NotFound())
            );
        };
        es.execute(r);
    }
}
