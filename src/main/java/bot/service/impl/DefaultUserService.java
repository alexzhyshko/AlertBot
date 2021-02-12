package bot.service.impl;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import bot.dao.UserDao;
import bot.model.User;
import bot.service.UserService;

@Component
public class DefaultUserService implements UserService{

    @Inject("defaultUserDao")
    private UserDao userDao;

    @Override
    public void saveUser(int userid, String username) {
       User newUser = User.builder().userId(userid).username(username).build();
       userDao.saveUser(newUser);
    }

    @Override
    public boolean userExists(int userid) {
       return userDao.userExists(userid);
    }

    @Override
    public boolean isBlacklisted(int userId) {
        return userDao.isBlacklisted(userId);
    }

    @Override
    public void setBlacklistedTrue(int userid) {
        userDao.setBlacklistedTrue(userid);
    }
    
}
