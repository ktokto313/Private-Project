package lkt.component;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBContext {
    private final static PGSimpleDataSource dataSource = new PGSimpleDataSource();

    static {
        //TODO: clean up
        dataSource.setUrl("jdbc:postgresql://db:5432/app");
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
