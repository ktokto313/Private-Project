package lkt.component;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBContext {
    private final static PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        dataSource.setUrl("jdbc:postgresql://db:5432/");
    }

    @Value("${postgres.db}")
    private void setDatabaseName(String databaseName) {
        dataSource.setDatabaseName(databaseName);
    }

    @Value("${postgres.user}")
    private void setUser(String user) {
        dataSource.setUser(user);
    }

    @Value("${postgres.password}")
    private void setPassword(String password) {
        dataSource.setPassword(password);
    }

    @Bean
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
