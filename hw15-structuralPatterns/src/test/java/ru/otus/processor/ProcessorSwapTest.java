package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorSwapFields;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorSwapTest {
    @Test
    public void swapFieldsTest() {
        Message message = new Message.Builder(1L)
                .field5("field5")
                .field11("field11")
                .field12("field12")
                .build();
        ProcessorSwapFields processorSwapFields = new ProcessorSwapFields();
        Message newMessage = processorSwapFields.process(message);

        assertEquals(message.getField12(), newMessage.getField11());
        assertEquals(message.getField11(), newMessage.getField12());
    }
}
