package ru.otus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ATMImpl implements ATM {

    private final Map<Denomination, Cell> cashHolder;

    public ATMImpl(Denomination[] denominations) {
        this.cashHolder = new HashMap<>();
        Arrays.stream(denominations).forEach(denomination -> cashHolder.put(denomination, new Cell(denomination)));
    }

    public int getBanknotesCount(Denomination denomination) {
        return cashHolder.get(denomination).getBanknotesCount();
    }

    public int getRemainderSum(Denomination denomination) {
        return cashHolder.get(denomination).getRemainderSum();
    }

    @Override
    public Map<Denomination, Integer> getBanknotes(int requestedSum) {
        if (requestedSum <= 0) {
            throw new IllegalArgumentException("Requested sum is null or negative");
        }
        Map<Denomination, Integer> cashForOut = new HashMap<>();
        Set<Denomination> sortedDenomination = cashHolder.keySet().stream().filter(k -> !cashHolder.get(k).isEmpty())
                .sorted(Comparator.comparing(Denomination::getValue))
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .reversed();
        int remainderSum = requestedSum;

        for (Denomination denomination: sortedDenomination) {
            int banknotesRequiredCount = remainderSum / denomination.getValue();
            if (banknotesRequiredCount > 0) {
                int banknotesOutCount = cashHolder.get(denomination).getBanknotes(banknotesRequiredCount);
                cashForOut.put(denomination, banknotesOutCount);
                remainderSum -= denomination.getValue() * banknotesOutCount;
            }

            if (remainderSum == 0) {
                break;
            }
        }
        if (remainderSum != 0) {
            cashForOut.forEach((key, value) -> cashHolder.get(key).putBanknotes(value));
            throw new IncorrectSumException("Requested sum can't be given");
        }
        return cashForOut;
    }

    @Override
    public void putBanknotes(Map<Denomination, Integer> depositedCash) {
        depositedCash.forEach( (den, count) -> cashHolder.get(den).putBanknotes(count) );
    }

    @Override
    public int getRemainderSum() {
        return cashHolder.values().stream().mapToInt(Cell::getRemainderSum).sum();
    }
}
