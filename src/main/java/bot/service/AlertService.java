package bot.service;

import java.util.List;

import bot.model.Alert;
import bot.model.User;

public interface AlertService {

    List<Alert> getAllUserAlerts(int userid);
    
    void createAlert(String name, String message, int ownerId);
    
    void updateAlertName(String oldname, String newname);
    
    void updateAlertMessage(String name, String newMessage);
    
    void deleteAlert(String name);
    
    List<User> getAllUsersByAlert(String name);
    
    Alert getAlertByName(String name);
}
