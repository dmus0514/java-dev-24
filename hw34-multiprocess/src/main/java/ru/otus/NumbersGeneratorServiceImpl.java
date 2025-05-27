package ru.otus;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumbersGeneratorServiceImpl extends NumbersGeneratorServiceGrpc.NumbersGeneratorServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumbersGeneratorServiceImpl.class);
    @Override
    public void generateNumber(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("generateNumber > Generating number from {} to {}", request.getFirstValue(), request.getLastValue());
        AtomicLong currentValue = new AtomicLong(request.getFirstValue());
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                long value = currentValue.incrementAndGet();
                log.info("generateNumber > send new value {}", value);
                responseObserver.onNext(NumberResponse.newBuilder().setNumber(value).build());
                if (value == request.getLastValue()) {
                    responseObserver.onCompleted();
                    log.info("generateNumber > exit ");
                    executor.shutdown();
                }
            }, 0, 2, TimeUnit.SECONDS);
    }
}
