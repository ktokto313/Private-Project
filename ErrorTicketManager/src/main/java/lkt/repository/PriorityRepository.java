package lkt.repository;

import lkt.mapper.Mapper;
import lkt.model.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriorityRepository implements IPriorityRepository {
    @Autowired
    private Connection connection;

    @Autowired
    private Mapper mapper;

    @Override
    public List<Priority> findAll() {
        List<Priority> priorities = new ArrayList<>();
        String sql = """
                select id, levelofpriority, name,
                       timetorespond,
                       timetofinish
                from priorities
                order by levelofpriority asc, id asc
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                priorities.add(mapper.mapPriority(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorities;
    }

    @Override
    public boolean update(Integer priorityID, int levelOfPriority, String name, String timeToRespond, String timeToFinish) {
        String sql = """
                update priorities
                set levelofpriority = ?,
                    name = ?,
                    timetorespond = ?::interval,
                    timetofinish = ?::interval
                where id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, levelOfPriority);
            preparedStatement.setString(2, name);
            preparedStatement.setObject(3, timeToRespond);
            preparedStatement.setObject(4, timeToFinish);
            preparedStatement.setInt(5, priorityID);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteByID(Integer priorityID) {
        String sql = "delete from priorities where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, priorityID);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Duration secondsToDuration(double seconds) {
        long millis = Math.round(seconds * 1000);
        return Duration.ofMillis(millis);
    }
}
