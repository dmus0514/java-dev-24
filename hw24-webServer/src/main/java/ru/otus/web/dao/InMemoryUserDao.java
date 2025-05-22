package ru.otus.web.dao;

import ru.otus.web.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    public static final String DEFAULT_PASSWORD = "11111";
    private final Map<String, User> users = new HashMap<>();

    public InMemoryUserDao() {
        users.put("admin", new User(1L, "Крис Гир", "admin", DEFAULT_PASSWORD));
        users.put("admin2", new User(2L, "Ая Кэш", "admin2", DEFAULT_PASSWORD));
    }

    @Override
    public Optional<User> findById(long id) {
        return users.values().stream().filter(v -> v.getId() == id).findFirst();
    }

    @Override
    public Optional<User> findRandomUser() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }
}
