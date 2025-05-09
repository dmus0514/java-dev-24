package ru.otus.web.dao;

import ru.otus.web.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    public static final String DEFAULT_PASSWORD = "11111";
    private final Map<Long, User> users = new HashMap<>();

    public InMemoryUserDao() {
        users.put(1L, new User(1L, "Крис Гир", "admin", DEFAULT_PASSWORD));
        users.put(2L, new User(2L, "Ая Кэш", "admin2", DEFAULT_PASSWORD));
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findRandomUser() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }
}
