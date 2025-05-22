package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
public record Address (
    @Id Long id,
    String street,
    @MappedCollection(idColumn = "address_id") Client client) {}
