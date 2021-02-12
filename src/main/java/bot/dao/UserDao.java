package bot.dao;

import bot.model.User;

public interface UserDao {

    void saveUser(User user);
    
    boolean userExists(int userId);
    
    boolean isBlacklisted(int userId);
    
    void setBlacklistedTrue(int userid);
}
