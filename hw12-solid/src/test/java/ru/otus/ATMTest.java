package ru.otus;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.Denomination.*;

@Slf4j
public class ATMTest {

    @Test
    void putBanknotesTest() {
        var atm = new ATMImpl();
        int cashSum = fillATM(atm);
        assertEquals(atm.getRemainderSum(), cashSum, "Sum in ATM is not equals to incoming cash sum!");
        assertEquals(atm.getCashHolder().get(FIVE_HUNDRED).getBanknotesCount(), 20, "Count of banknotes in FIVE_HUNDRED cell is not valid!");
        assertEquals(atm.getCashHolder().get(ONE_HUNDRED).getRemainderSum(), 1000, "Sum of banknotes in ONE_HUNDRED cell is not valid!");
    }

    @Test
    void getBanknotesTest() {
        var atm = new ATMImpl();
        int cashSum = fillATM(atm);
        Map<Denomination, Integer> cashBanknotes = atm.getBanknotes(25_000);
        assertEquals(cashBanknotes.get(ONE_THOUSAND), 25, "Given count of ONE_THOUSAND banknotes is not valid");
        assertEquals(atm.getRemainderSum(), cashSum - 25_000, "Remainder Sum in ATM is not valid");

    }

    @Test
    void getBanknotesTest2() {
        var atm = new ATMImpl();
        int cashSum = fillATM(atm);
        Map<Denomination, Integer> cashBanknotes = atm.getBanknotes(30_740);
        assertEquals(30, cashBanknotes.get(ONE_THOUSAND), "Given count of ONE_THOUSAND banknotes is not valid");
        assertEquals(1, cashBanknotes.get(FIVE_HUNDRED), "Given count of FIVE_HUNDRED banknotes is not valid");
        assertEquals(2, cashBanknotes.get(ONE_HUNDRED), "Given count of ONE_HUNDRED banknotes is not valid");
        assertEquals(4, cashBanknotes.get(TEN), "Given count of TEN banknotes is not valid");
        assertEquals(cashSum - 30_740, atm.getRemainderSum(), "Remainder Sum in ATM is not valid");

    }

    @Test
    void getBanknotesTest3() {
        var atm = new ATMImpl();
        int cashSum = fillATM(atm);
        assertThrows(IncorrectSumException.class, () -> atm.getBanknotes(64_560));

    }

    @Test
    void getBanknotesTest4() {
        var atm = new ATMImpl();
        int cashSum = fillATM(atm);
        assertThrows(IncorrectSumException.class, () -> atm.getBanknotes(32_477));

    }

    Integer fillATM (ATMImpl atm) {
        Map<Denomination, Integer> depositedCash = Map.of(TEN, 100, FIFTY, 20, ONE_HUNDRED, 10, FIVE_HUNDRED, 20, ONE_THOUSAND, 50);
        int cashSum = depositedCash.entrySet().stream().mapToInt(entry -> (entry.getKey().getValue() * entry.getValue())).sum();
        atm.putBanknotes(depositedCash);
        return cashSum;
    }
}
