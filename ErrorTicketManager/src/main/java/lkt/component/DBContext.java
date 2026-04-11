package lkt.component;

import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPooledConnection;
import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc2.optional.PoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBContext {
    private final static PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        //TODO: clean up
        dataSource.setUrl("jdbc:postgresql://localhost:5432/app");
        dataSource.setUser("sa");
        dataSource.setPassword("example");
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
