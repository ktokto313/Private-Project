package lkt.repository;

import lkt.mapper.Mapper;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository implements IUserRepository {
    @Autowired
    private Connection connection;
    @Autowired
    private Mapper mapper;

    @Override
    public User findByUsername(String username) {
        try {
            //TODO mapper here only take one user from the result set
            String sql = "select * from users where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findByUserID(Integer userID) {
        try {
            String sql = "select * from users where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User insert(User user) {
        try {
            String sql = "insert into users (id, role, username, password, departmentid) values {?, ?, ?, ?, ?}";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getUserID());
            preparedStatement.setString(2, user.getRole().toString());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getDepartmentID());
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.getUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            // Exception can happen when role is wrong or department_id is not correct
            return null;
        }
    }

    @Override
    public User update(User user) {
        try {
            String sql = "update users set role=?::role, username=?, password=?, departmentid=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getRole().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getDepartmentID());
            preparedStatement.setInt(5, user.getUserID());
            if (preparedStatement.executeUpdate() == 1) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(User user) {
        try {
            String sql = "delete from users where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getUserID());
            if (preparedStatement.executeUpdate() == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
