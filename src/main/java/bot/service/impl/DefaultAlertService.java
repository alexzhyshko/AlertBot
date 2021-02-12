package bot.service.impl;

import java.util.List;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import bot.dao.AlertDao;
import bot.model.Alert;
import bot.model.User;
import bot.service.AlertService;

@Component
public class DefaultAlertService implements AlertService{

    @Inject("defaultAlertDao")
    private AlertDao alertDao;

    @Override
    public void deleteAlert(String alertName) {
        alertDao.removeAlert(alertName);
    }

    @Override
    public void createAlert(String name, String message, int ownerId) {
        alertDao.saveAlert(name, message, ownerId);
    }

    @Override
    public void updateAlertName(String oldname, String newname) {
        alertDao.updateAlertName(oldname, newname);
    }

    @Override
    public void updateAlertMessage(String name, String newMessage) {
        alertDao.updateAlertMessage(name, newMessage);
    }

    @Override
    public List<Alert> getAllUserAlerts(int userid) {
        return alertDao.getAlertsByUserId(userid);
    }

    @Override
    public List<User> getAllUsersByAlert(String name) {
        return alertDao.getAllUsersByAlert(name);
    }
    
    @Override
    public Alert getAlertByName(String name) {
        return alertDao.getAlertByName(name);
    }
    
    
    
}
