package homework;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {

    private final Deque<Customer> customerStore = new LinkedList<>();

    public void add(Customer customer) {
        customerStore.add(customer);
    }

    public Customer take() {
        return customerStore.removeLast();
    }
}
