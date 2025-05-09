package ru.otus.data.crm.dto;

import com.google.gson.annotations.Expose;
import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;

import java.util.List;

public record ClientRecord(@Expose String name, @Expose String address, @Expose String phone) {
    public Client build() {
        return new Client(null, name, new Address(null, address), List.of(new Phone(null, phone)));
    }
}
