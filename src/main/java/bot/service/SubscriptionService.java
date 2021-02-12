package bot.service;

public interface SubscriptionService {

    void subscribeUserToAlert(int userid, String alertName); 
    
    void unsubscribeUserFromAlert(int userid, String alertName);
    
}
