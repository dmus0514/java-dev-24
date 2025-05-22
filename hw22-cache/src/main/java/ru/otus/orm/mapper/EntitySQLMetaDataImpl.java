package ru.otus.orm.mapper;

import ru.otus.orm.mapper.EntityClassMetaData;
import ru.otus.orm.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("SELECT * FROM %s WHERE %s = ?", entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String fieldsWithoutIdParam = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(","));
        String valuesParam = "?,".repeat(entityClassMetaData.getFieldsWithoutId().size() - 1).concat("?");

        return String.format("INSERT INTO %s(%s) VALUES (%s)", entityClassMetaData.getName(), fieldsWithoutIdParam,
                valuesParam);
    }

    @Override
    public String getUpdateSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?", entityClassMetaData.getName(), fields,
                entityClassMetaData.getIdField().getName());
    }
}
