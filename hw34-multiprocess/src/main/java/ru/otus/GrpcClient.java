package ru.otus;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {
    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    private static final int LOOP_LIMIT = 50;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    public static final int FIRST_VALUE = 0;
    public static final int LAST_VALUE = 30;

    public static void main(String[] args) {
        log.info("GrpcClient > start");
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var asyncStub = NumbersGeneratorServiceGrpc.newStub(channel);
        ClientStreamObserver clientStreamObserver = new ClientStreamObserver();
        asyncStub.generateNumber(NumberRequest.newBuilder().setFirstValue(FIRST_VALUE).setLastValue(LAST_VALUE).build(),
                clientStreamObserver);
        long value = 0;
        for (int i = 0; i < LOOP_LIMIT; i++) {
            value = value + clientStreamObserver.getAndResetValue() + 1;
            log.info("Current Value:{}", value);
            sleep();
        }

        log.info("GrpcClient > exit");
        channel.shutdown();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
