package ru.otus;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class GrpcServer  {
    public static final int SERVER_PORT = 8190;
    private static final Logger log = LoggerFactory.getLogger(GrpcServer.class);

    public static void main(String[] args) throws InterruptedException, IOException {

        Server server = ServerBuilder.forPort(SERVER_PORT).addService(new NumbersGeneratorServiceImpl()).build();
        server.start();
        log.info("GrpcServer > Server waiting for client connections...");

        server.awaitTermination();
    }
}
