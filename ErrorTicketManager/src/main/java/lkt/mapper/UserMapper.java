package lkt.mapper;

import lkt.model.Department;
import lkt.model.User;
import lkt.util.Util;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper {
    public User getUser(ResultSet resultSet) throws SQLException {
        resultSet.next();
        User user = new User();
        user.setUserID(resultSet.getInt("id"));
        user.setRole(Util.getRoleFromString(resultSet.getString("role")));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        Department department = new Department();
        department.setID(resultSet.getInt("departmentid"));
        user.setDepartment(department);
        return user;
    }
}
