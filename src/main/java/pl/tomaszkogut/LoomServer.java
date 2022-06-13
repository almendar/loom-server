package pl.tomaszkogut;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoomServer extends Server {

    private ExecutorService es;

    public LoomServer(Database db) {
        super(db);
        es = Executors.newVirtualThreadPerTaskExecutor();
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
        es.submit(r);
    }
}
