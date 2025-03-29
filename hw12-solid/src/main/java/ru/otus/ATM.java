package ru.otus;

import java.util.Map;

public interface ATM {
    Map<Denomination, Integer> getBanknotes(int requestedSum);
    void putBanknotes(Map<Denomination, Integer> depositedCash);
    int getRemainderSum();

}
