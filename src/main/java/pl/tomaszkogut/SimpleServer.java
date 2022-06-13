package pl.tomaszkogut;

public class SimpleServer extends Server {

    public SimpleServer(Database db) {
        super(db);
    }

    @Override
    void handleConnection(Connection c) {
        var maybeUser = this.db.get(c.req().id());
        maybeUser.ifPresentOrElse(
                user -> c.write(new Ok(user)),
                () -> c.write(new NotFound())
        );
    }
}
