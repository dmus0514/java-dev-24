package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        Comparator<SensorData> comparator = Comparator.comparing(SensorData::getMeasurementTime);
        dataBuffer = new PriorityBlockingQueue<>(bufferSize, comparator);
    }

    @Override
    public void process(SensorData data) {
        boolean elemAdded = dataBuffer.offer(data);
        if (!elemAdded) {
            log.error("Element was not added! {}", data);
        }

        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        List<SensorData> bufferedData = new ArrayList<>(bufferSize);
        try {
            dataBuffer.drainTo(bufferedData);
            if (!bufferedData.isEmpty()) {
                writer.writeBufferedData(bufferedData);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
