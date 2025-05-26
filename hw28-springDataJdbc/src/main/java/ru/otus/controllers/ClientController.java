package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.dto.ClientRecord;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceAddress;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final DBServiceClient clientService;
    private final DBServiceAddress addressService;
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    @PostMapping("/api/client")
    public Client create(@RequestBody ClientRecord clientDto) {
        Set<Phone> phones = clientDto.phones().stream().map(phone -> new Phone(null, phone, null)).collect(Collectors.toSet());
        Address address = new Address(null, clientDto.address(), null);
        var client = new Client(null, clientDto.name(), address, phones);
        log.info("Prepared client: {}", client);
        client = clientService.saveClient(client);
        return client;
    }

    @GetMapping("/api/client/{id}")
    public Client read(@PathVariable long id) {
        return clientService.getClient(id).orElse(null);
    }

    @GetMapping("/api/client")
    public List<ClientRecord> readAll() {

        return clientService.findAll().stream().map(client ->
                new ClientRecord(
                        client.getName(),
                        client.getAddress().street(),
                        /*addressService.getAddress(client.getAddress_id()).get().street(),*/
                        client.getPhones().stream().map(Phone::number).toList())
        ).toList();
    }
}
