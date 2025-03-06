package ru.otus.processor.homework;

import ru.otus.processor.Processor;
import ru.otus.model.Message;

public class ProcessorSwapFields implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field12 = message.getField12();

        return message.toBuilder()
                .field11(field12)
                .field12(field11)
                .build();
    }

}
