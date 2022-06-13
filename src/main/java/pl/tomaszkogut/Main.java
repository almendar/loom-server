package pl.tomaszkogut;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws Exception {
        var server = new LoomServer(new Database());
        server.start();


        var connection = new LinkedList<Server.Connection>();

        for (int i = 0; i < 5000; i++) {
            connection.add(server.server(new Server.Request(i)));
            System.out.println("Adding connections");
        }

        connection.forEach(c -> {
            try {
                System.out.println("Reading connections");
                c.body().take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


//        var clients = List.of(
//                new Client(server, new Server.Request(1)),
//                new Client(server, new Server.Request(2)),
//                new Client(server, new Server.Request(3)),
//                new Client(server, new Server.Request(4))
//        );
//        clients.forEach(c -> c.call());
//        clients.forEach(c -> c.waitForAnswer());

        server.stop();

        server.close();
    }

}