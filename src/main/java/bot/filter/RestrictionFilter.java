package bot.filter;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.boilerplate.MessageSender;
import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Filter;
import application.context.annotation.Inject;
import application.context.filter.FilterAdapter;
import application.exception.FilterException;
import bot.service.UserService;

@Component
@Filter(order=0, enabled=true)
public class RestrictionFilter implements FilterAdapter{

    @Inject("defaultUserService")
    private UserService userService;
    
    @Inject
    private MessageSender sender;
    
    @Override
    public Update filter(Update update) throws FilterException {
        int userid = ApplicationContext.getCurrentUserId();
        if(userService.isBlacklisted(userid)) {
            sender.setText("Ты в бане. Если думаешь, что это ошибка, то подумай еще раз получше");
            sender.setChatId(userid);
            sender.sendMessage();
            throw new FilterException("User is blacklisted");
        }
        return update;
    }

    
    
}
