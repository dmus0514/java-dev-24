package ru.otus.crm.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

public record ClientRecord(@Expose String name, @Expose String address, @Expose List<String> phones) {}
