package bot.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import bot.connection.ConnectionManager;
import bot.dao.AlertDao;
import bot.dao.SubscriptionDao;
import bot.model.Alert;
import bot.model.User;

@Component
public class DefaultAlertDao implements AlertDao{

    private static String GET_ALERTS_BY_USER_ID_QUERY = "SELECT owner_id, name as alert_name, message as alert_message, username as owner_username FROM Alerts a JOIN Subscriptions s ON a.id = s.alert_id JOIN `Users` u ON u.id = s.user_id WHERE u.id=?;";
    private static String GET_ALERTS_BY_ALERT_NAME_QUERY = "SELECT u.id as user_id, name as alert_name, message as alert_message, u.username as username FROM Alerts a JOIN Subscriptions s ON a.id = s.alert_id JOIN `Users` u ON u.id = s.user_id WHERE a.name=?;";
    private static String SAVE_ALERT_QUERY = "INSERT INTO Alerts(name, message,owner_id) VALUES(?,?,?);";
    private static String REMOVE_ALERT_QUERY = "DELETE FROM Alerts WHERE name=?;";
    private static String UPDATE_ALERT_NAME_QUERY = "UPDATE Alerts SET name=? WHERE name=?";
    private static String UPDATE_ALERT_MESSAGE_QUERY = "UPDATE Alerts SET message=? WHERE name=?";
    private static String GET_ALERT_BY_NAME_QUERY = "SELECT a.name as alert_name, a.message as alert_message, u.id as owner_id, u.username as owner_username FROM Alerts a JOIN `Users` u ON a.owner_id = u.id WHERE a.name=?";
    
    @Inject("defaultSubscriptionDao")
    private SubscriptionDao subscriptionDao;
    
    @Override
    public List<Alert> getAlertsByUserId(int userid) {
        List<Alert> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(GET_ALERTS_BY_USER_ID_QUERY)){
                statement.setInt(1, userid);
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()) {
                        User owner = User.builder()
                                .userId(rs.getInt("owner_id"))
                                .username(rs.getString("owner_username"))
                                .build();
                        Alert alert = Alert.builder()
                                .name(rs.getString("alert_name"))
                                .message(rs.getString("alert_message"))
                                .owner(owner)
                                .build();
                        result.add(alert);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveAlert(String name, String message, int ownerId) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(SAVE_ALERT_QUERY)){
                statement.setString(1, name);
                statement.setString(2, message);
                statement.setInt(3, ownerId);
                statement.executeUpdate();
                
                subscriptionDao.subscribeUserToAlertByAlertName(ownerId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAlert(String alertName) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(REMOVE_ALERT_QUERY)){
                
                subscriptionDao.unsubscribeAll(alertName);
                
                removeAlertStatement.setString(1, alertName);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAlertName(String oldName, String newName) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(UPDATE_ALERT_NAME_QUERY)){
                removeAlertStatement.setString(1, newName);
                removeAlertStatement.setString(2, oldName);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAlertMessage(String name, String newMessage) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(UPDATE_ALERT_MESSAGE_QUERY)){
                removeAlertStatement.setString(1, newMessage);
                removeAlertStatement.setString(2, name);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsersByAlert(String name) {
        List<User> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(GET_ALERTS_BY_ALERT_NAME_QUERY)){
                statement.setString(1, name);
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()) {
                        User user = User.builder()
                                .userId(rs.getInt("user_id"))
                                .username(rs.getString("username"))
                                .build();
                        result.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Alert getAlertByName(String name) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement statement = connection.prepareStatement(GET_ALERT_BY_NAME_QUERY)){
                statement.setString(1, name);
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()) {
                        User owner = User.builder().userId(rs.getInt("owner_id")).username(rs.getString("owner_username")).build();
                        return Alert.builder().name(rs.getString("alert_name")).message(rs.getString("alert_message")).owner(owner).build();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}
