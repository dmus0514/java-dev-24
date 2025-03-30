package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::findByIdRSHandler);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), this::findAllRSHandler)
                .orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldValues(client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getFieldValues(client));
    }

    private T findByIdRSHandler(ResultSet rs) {
        try {
            if (rs.next()) {
                T object = entityClassMetaData.getConstructor().newInstance();
                for (Field field : entityClassMetaData.getAllFields()) {
                    field.setAccessible(true);
                    field.set(object, rs.getObject(field.getName()));
                }
                return object;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> findAllRSHandler(ResultSet rs) {
        List<T> resultList = new ArrayList<>();
        try {
            while (rs.next()) {
                resultList.add(findByIdRSHandler(rs));
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getFieldValues(T client) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            field.setAccessible(true);
            try {
                return field.get(client);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
