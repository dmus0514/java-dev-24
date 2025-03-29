package ru.otus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Cell {
    private final Denomination denomination;
    private int banknotesCount;

    public Cell(Denomination denomination) {
        if (denomination == null) {
            throw new IllegalArgumentException("Denomination is null");
        }
        this.denomination = denomination;
        banknotesCount = 0;
    }

    public int getBanknotes(int requestedCount) {
        int returnedCount;
        if (requestedCount >= banknotesCount) {
            returnedCount = banknotesCount;
            banknotesCount = 0;

        } else {
            returnedCount = requestedCount;
            banknotesCount -= requestedCount;
        }
        return returnedCount;
    }

    public void putBanknotes(int count) {
        banknotesCount += count;
    }

    public int getRemainderSum() {
        return banknotesCount * denomination.getValue();
    }

    public boolean isEmpty() {
        return banknotesCount == 0;
    }

}
