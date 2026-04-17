package lkt.repository;

import lkt.mapper.Mapper;
import lkt.model.TicketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketTypeRepository implements ITicketTypeRepository {
    @Autowired
    private Connection connection;

    @Autowired
    private Mapper mapper;

    @Override
    public List<TicketType> findAll() {
        List<TicketType> ticketTypes = new ArrayList<>();
        String sql = """
                select id, title, description
                from tickettypes
                order by title asc, id asc
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ticketTypes.add(mapper.mapTicketTypeRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticketTypes;
    }

    @Override
    public TicketType findByID(int id) {
        String sql = """
                select id, title, description
                from tickettypes
                where id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return mapper.mapTicketTypeRow(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TicketType insert(TicketType ticketType) {
        String sql = """
                insert into tickettypes (title, description)
                values (?, ?)
                returning id, title, description
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ticketType.getTitle());
            preparedStatement.setString(2, ticketType.getDescription());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapTicketTypeRow(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

