package bot.service.impl;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import bot.dao.SubscriptionDao;
import bot.service.SubscriptionService;

@Component
public class DefaultSubscriptionService implements SubscriptionService{

    @Inject("defaultSubscriptionDao")
    private SubscriptionDao subscriptionDao;
    
    @Override
    public void subscribeUserToAlert(int userid, String alertName) {
        subscriptionDao.subscribeUserToAlertByAlertName(userid, alertName);
    }

    @Override
    public void unsubscribeUserFromAlert(int userid, String alertName) {
        subscriptionDao.unsubscribeUserFromAlertByAlertName(userid, alertName);
    }

}
