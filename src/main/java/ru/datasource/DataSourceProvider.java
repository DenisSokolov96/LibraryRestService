package ru.datasource;

import org.jooq.SQLDialect;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource provide();

    SQLDialect sqlDialect();
}
