package bot.service;

public interface UserService {

    void saveUser(int userid, String username);
    
    boolean userExists(int userid);
}
