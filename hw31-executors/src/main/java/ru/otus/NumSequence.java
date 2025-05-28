package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumSequence {
    private static final Logger logger = LoggerFactory.getLogger(NumSequence.class);
    private String last = "second";
    private boolean isReverse = false;
    private boolean needChange = false;
    private int value = 1;

    private synchronized void action(String order) {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(order)) {
                    this.wait();
                }

                logger.info("{}", value);
                if (needChange) {
                    value += isReverse ? -1 : 1;
                }

                last = order;

                if (value <= 1 && isReverse) {
                    isReverse = false;
                } else if (value == 10) {
                    isReverse = true;
                }
                needChange = !needChange;

                sleep();
                notifyAll();
                //logger.info("after notify");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public static void main(String[] args) {
        var numSequence = new NumSequence();
        new Thread(() -> numSequence.action("first"), "firstThread").start();
        new Thread(() -> numSequence.action("second"), "secondThread").start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}