package bot.dao;

import bot.model.User;

public interface UserDao {

    void saveUser(User user);
    
    boolean userExists(int userId);
}
