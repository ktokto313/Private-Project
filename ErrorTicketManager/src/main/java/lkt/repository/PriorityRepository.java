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
    public Priority insert(Priority priority) {
        String sql = """
                insert into priorities (levelofpriority, name, timetorespond, timetofinish)
                values (?, ?, ?::interval, ?::interval)
                returning id, levelofpriority, name, timetorespond, timetofinish
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, priority.getLevelOfPriority());
            preparedStatement.setString(2, priority.getName());
            preparedStatement.setObject(3, priority.getTimeToRespond());
            preparedStatement.setObject(4, priority.getTimeToFinish());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapPriority(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Priority priority) {
        String sql = """
                update priorities
                set levelofpriority = ?,
                    name = ?,
                    timetorespond = ?::interval,
                    timetofinish = ?::interval
                where id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, priority.getLevelOfPriority());
            preparedStatement.setString(2, priority.getName());
            preparedStatement.setObject(3, priority.getTimeToRespond());
            preparedStatement.setObject(4, priority.getTimeToFinish());
            preparedStatement.setInt(5, priority.getID());
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
}
