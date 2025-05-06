package ru.otus.orm.core.sessionmanager;

import ru.otus.orm.core.sessionmanager.TransactionAction;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}
