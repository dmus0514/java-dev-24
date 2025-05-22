package ru.otus.orm.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.orm.core.repository.DataTemplate;
import ru.otus.orm.core.sessionmanager.TransactionRunner;
import ru.otus.orm.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<Long, Client> cache;


    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<Long, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
        cache.put(savedClient.getId(), savedClient);

        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientFromCache = cache.get(id);
        if (clientFromCache != null) {
            return Optional.of(clientFromCache);
        } else {
            var clientOptional = transactionRunner.doInTransaction(connection -> dataTemplate.findById(connection, id));
            if (clientOptional.isPresent()) {
                var clientFromDB = clientOptional.get();
                cache.put(clientFromDB.getId(), clientFromDB);
            }
            return clientOptional;
        }
    }

    @Override
    public List<Client> findAll() {
        var clientList = transactionRunner.doInTransaction(connection -> dataTemplate.findAll(connection));
        cache.removeAll(); //to remove all old values that not actual
        clientList.forEach(client -> cache.put(client.getId(), client));
        return clientList;
    }
}
