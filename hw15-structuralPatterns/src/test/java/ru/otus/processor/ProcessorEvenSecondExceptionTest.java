package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorEvenSecondException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessorEvenSecondExceptionTest {

    @Test
    public void exceptionWhenEvenSecond() {
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 3, 4, 22, 40, 2);
        Message message = new Message.Builder(4L).field11("test").build();
        ProcessorEvenSecondException processor = new ProcessorEvenSecondException(() -> currentDateTime);

        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    public void noExceptionWhenOddSecond() {
        LocalDateTime currentDateTime = LocalDateTime.of(2025, 3, 4, 22, 40, 1);
        Message message = new Message.Builder(3L).field12("test3").build();
        ProcessorEvenSecondException processor = new ProcessorEvenSecondException(() -> currentDateTime);

        assertDoesNotThrow(() -> processor.process(message));
    }
}
