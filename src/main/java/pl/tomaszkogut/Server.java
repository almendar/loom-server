package pl.tomaszkogut;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

abstract public class Server implements AutoCloseable {

    protected final Database db;
    private final AtomicBoolean shouldRun;

    private final Thread runningThread;


    public record Connection(Server.Request req, ArrayBlockingQueue<Server.Response> body) {

        public void write(Server.Response res) {
            try {
                body.put(res);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static record Request(long id) {
    }

    ;

    public static interface Response {
    }

    ;

    public static record Ok(User body) implements Response {
    }

    ;

    public static record NotFound() implements Response {
    }

    ;




    private LinkedBlockingQueue<Connection> incomingConnections;

    public Server(Database db) {
        this.db = db;
        this.incomingConnections = new LinkedBlockingQueue<>();
        shouldRun = new AtomicBoolean(true);
        runningThread = new Thread(this::loop);
    }

    public void stop() {
        shouldRun.set(false);
        runningThread.interrupt();
    }


    public Connection server(Request r) {
        var c = new Connection(r, new ArrayBlockingQueue<>(1));
        incomingConnections.add(c);
        return c;
    }


    public void start() {
        runningThread.start();
    }

    private void loop() {
        while (shouldRun.get()) {
            try {
                var connection = incomingConnections.take();
                handleConnection(connection);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        }
    }

    public void close() throws Exception {
        db.close();
    }

    abstract void handleConnection(Connection c);
}
