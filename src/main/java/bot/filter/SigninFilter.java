package bot.filter;

import java.util.UUID;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Filter;
import application.context.annotation.Inject;
import application.context.filter.FilterAdapter;
import application.exception.FilterException;
import application.routing.RouterManager;
import bot.service.UserService;
import bot.states.MainMenuState;

@Component
@Filter(order=1, enabled=true)
public class SigninFilter implements FilterAdapter{

    @Inject("defaultUserService")
    private UserService userService;
    
    @Inject
    private RouterManager router;
    
    @Override
    public Update filter(Update update) throws FilterException {
        int userId = ApplicationContext.getCurrentUserId();
        int userState = ApplicationContext.getCurrentUserState();
        if(userState==0 || !userService.userExists(userId)) {
            String username;
            if(update.hasCallbackQuery()) {
                username = update.getCallbackQuery().getFrom().getUserName();
            } else {
                username = update.getMessage().getFrom().getUserName();
            }
            if(username == null || username.isEmpty()) {
                username = UUID.randomUUID().toString();
            }
            userService.saveUser(userId, username);
            router.routeToClass(userId, MainMenuState.class);
        }
        return update;
    }
    
}
