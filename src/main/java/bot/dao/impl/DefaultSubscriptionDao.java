package bot.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.context.annotation.Component;
import bot.connection.ConnectionManager;
import bot.dao.SubscriptionDao;

@Component
public class DefaultSubscriptionDao implements SubscriptionDao{

    private static String SUBSCRIBE_USER_TO_ALERT_QUERY = "INSERT INTO Subscriptions(user_id, alert_id) VALUES(?, (SELECT id FROM Alerts WHERE name=?))";
    private static String UBSUBSCRIBE_USER_FROM_ALERT_QUERY = "DELETE FROM Subscriptions WHERE user_id=? AND alert_id =(SELECT id FROM Alerts WHERE name=?)";
    private static String UBSUBSCRIBE_ALL_FROM_ALERT_QUERY = "DELETE FROM Subscriptions s WHERE alert_id =(SELECT id FROM Alerts WHERE name=?)";
    
    @Override
    public void subscribeUserToAlertByAlertName(int userId, String alertName) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(SUBSCRIBE_USER_TO_ALERT_QUERY)){
                removeAlertStatement.setInt(1, userId);
                removeAlertStatement.setString(2, alertName);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribeUserFromAlertByAlertName(int userId, String alertName) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(UBSUBSCRIBE_USER_FROM_ALERT_QUERY)){
                removeAlertStatement.setInt(1, userId);
                removeAlertStatement.setString(2, alertName);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribeAll(String alertName) {
        try (Connection connection = ConnectionManager.createConnection();) {
            try(PreparedStatement removeAlertStatement = connection.prepareStatement(UBSUBSCRIBE_ALL_FROM_ALERT_QUERY)){
                removeAlertStatement.setString(1, alertName);
                removeAlertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
