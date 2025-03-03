package ru.otus;

import java.util.Map;

public interface ATM {
    Map<Denomination, Integer> getBanknotes(Integer requestedSum);
    void putBanknotes(Map<Denomination, Integer> depositedCash);
    int getRemainderSum();

}
