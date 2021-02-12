package bot.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.context.annotation.Component;
import bot.connection.ConnectionManager;
import bot.dao.UserDao;
import bot.model.Alert;
import bot.model.User;

@Component
public class DefaultUserDao implements UserDao{

    private static String SAVE_USER_QUERY = "INSERT INTO `Users`(id, username) VALUES(?,?);";
    private static String USER_EXISTS_QUERY = "SELECT EXISTS(SELECT username FROM `Users` WHERE id=?) as result";
    
    @Override
    public void saveUser(User user) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(SAVE_USER_QUERY)){
                statement.setInt(1, user.getUserId());
                statement.setString(2, user.getUsername());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public boolean userExists(int userId) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(USER_EXISTS_QUERY)){
                statement.setInt(1, userId);
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()) {
                        return rs.getBoolean("result");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
