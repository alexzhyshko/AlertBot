package bot.dao;

public interface SubscriptionDao {

    void subscribeUserToAlertByAlertName(int userId, String alertName);
    
    void unsubscribeUserFromAlertByAlertName(int userId, String alertName);
    
    void unsubscribeAll(String alertName);
    
}
