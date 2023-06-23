package ru.datasource;

import jakarta.enterprise.context.ApplicationScoped;
import org.jooq.SQLDialect;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
public class CustomDataSourceProvider implements DataSourceProvider{
    @Override
    public DataSource provide() {
        String dataSourceName = "java:jboss/datasources/Library";
        return lookupDataSource(dataSourceName);
    }

    @Override
    public SQLDialect sqlDialect() {
        return SQLDialect.POSTGRES;
    }

    private DataSource lookupDataSource(String dataSourceName) {
        try {
            return (DataSource) new InitialContext().lookup(dataSourceName);
        } catch (NamingException e) {
            throw new IllegalStateException("Ошибка получения DataSource : " + dataSourceName, e);
        }
    }
}
