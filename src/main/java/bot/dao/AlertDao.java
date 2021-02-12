package bot.dao;

import java.util.List;

import bot.model.Alert;
import bot.model.User;

public interface AlertDao {
    
    List<Alert> getAlertsByUserId(int userid);
    
    void saveAlert(String name, String message, int ownerId);

    void removeAlert(String name);
    
    void updateAlertName(String oldName, String newName);
    
    void updateAlertMessage(String name, String newMessage);
    
    List<User> getAllUsersByAlert(String name);
    
    Alert getAlertByName(String name);
}
