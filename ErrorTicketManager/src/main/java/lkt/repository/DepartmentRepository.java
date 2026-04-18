package lkt.repository;

import lkt.mapper.Mapper;
import lkt.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository implements IDepartmentRepository {
    @Autowired
    private Connection connection;

    @Autowired
    private Mapper mapper;

    @Override
    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        String sql = """
                select id, name
                from departments
                order by name asc, id asc
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                departments.add(mapper.mapDepartmentRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Department findByID(int id) {
        String sql = """
                select id, name
                from departments
                where id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapper.mapDepartmentRow(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
