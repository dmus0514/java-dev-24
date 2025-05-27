package ru.otus;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);
    private long value = 0;

    @Override
    public void onNext(NumberResponse numberResponse) {
        long receivedValue = numberResponse.getNumber();
        log.info("New Value:{}", receivedValue);
        value = receivedValue;
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Error:", throwable);
    }

    @Override
    public void onCompleted() {
        log.info("Request completed");
    }

    public long getAndResetValue() {
        long tmp = value;
        value = 0;
        return tmp;
    }

}
