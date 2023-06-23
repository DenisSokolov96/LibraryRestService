package context;

import org.h2.jdbcx.JdbcDataSource;
import org.jooq.SQLDialect;
import ru.datasource.DataSourceProvider;
import ru.libraryService.LibraryService;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class Context {

    public LibraryService initLibraryService() {
        Properties config = readConfigProperties();
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(config.getProperty("url"));
        dataSource.setUser(config.getProperty("username"));
        dataSource.setPassword(config.getProperty("password"));
        DataSourceProvider dsProvider = new DataSourceProvider() {
            @Override
            public DataSource provide() {
                return dataSource;
            }

            @Override
            public SQLDialect sqlDialect() {
                return SQLDialect.H2;
            }
        };
        LibraryService libraryService = new LibraryService(dsProvider);
        libraryService.migrate();
        return libraryService;
    }

    private Properties readConfigProperties() {
        Properties props = new Properties();
        try {
            props.load(Context.class.getResourceAsStream("/db_config.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return props;
    }
}
