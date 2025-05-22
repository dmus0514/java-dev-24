package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Table(name = "client")
public final class Client {
    @Id
    private final Long id;
    private final String name;
    private Long address_id;
    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(
            Long id,
            String name,
            Long address_id,
            Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address_id = address_id;
        this.phones = phones;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Client) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.address_id, that.address_id) &&
                Objects.equals(this.phones, that.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address_id, phones);
    }

    @Override
    public String toString() {
        return "Client[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "address_id=" + address_id + ", " +
                "phones=" + phones + ']';
    }
}

