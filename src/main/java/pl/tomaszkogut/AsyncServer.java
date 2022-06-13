package pl.tomaszkogut;

public class AsyncServer extends Server {

    public AsyncServer(Database db) {
        super(db);
    }

    @Override
    void handleConnection(Connection c) {
//        System.out.println("[Server] Accepting connection");
        db.getAsync(c.req().id()).thenAccept(maybeUser -> {
            maybeUser.ifPresentOrElse(
                    user -> c.write(new Ok(user)),
                    () -> c.write(new NotFound()));
        });
    }
}
