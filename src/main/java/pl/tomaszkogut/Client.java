package pl.tomaszkogut;


public class Client {
    private Server server;
    private Server.Request req;

    private Thread runningThread;

    public Client(Server server, Server.Request req) {
        this.server = server;
        this.req = req;
        this.runningThread = new Thread(this::run);

    }

    public void call() {
        runningThread.start();
    }


    public void waitForAnswer() {
        try {
            runningThread.join();
        } catch (InterruptedException e) {

        }
    }

    private void run() {
        System.out.println("[Client-" + req.id() + "] Starting!");

        var conn = server.server(req);
        try {
            var body = conn.body().take();
            switch (body) {
                case Server.NotFound nf -> System.out.println("User not found");
                case Server.Ok ok -> System.out.println("User found: " + ok.body());
                default -> throw new IllegalStateException("Unexpected value: " + body);
            }
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        }
        System.out.println("[Client-" + req.id() + "] bye!");
    }
}

