package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customerStore = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = customerStore.firstEntry();
        final Customer cust = firstEntry.getKey();
        return Map.entry(new Customer(cust.getId(), cust.getName(), cust.getScores()), firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = customerStore.higherEntry(customer);

        if (higherEntry == null) return null;

        final Customer cust = higherEntry.getKey();
        return Map.entry(new Customer(cust.getId(), cust.getName(), cust.getScores()), higherEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customerStore.put(customer, data);
    }
}
