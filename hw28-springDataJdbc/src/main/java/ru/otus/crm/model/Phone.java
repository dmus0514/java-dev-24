package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
public record Phone (
        @Id Long id,
        String number,
        Long client_id) {}
