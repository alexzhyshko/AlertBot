package bot.states;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.boilerplate.MessageDeleter;
import application.boilerplate.MessageEditor;
import application.boilerplate.MessageSender;
import application.boilerplate.dto.InlineButton;
import application.context.ApplicationContext;
import application.context.annotation.Callback;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.Message;
import application.context.annotation.State;
import application.routing.RouterManager;
import application.session.SessionManager;
import bot.Main;
import bot.service.UserService;
import bot.util.CallbackUtils;

@Component
@State(5)
public class BlacklistState {

    @Inject
    private MessageSender sender;
    
    @Inject
    private MessageEditor editor;

    @Inject
    private MessageDeleter deleter;
    
    @Inject("defaultUserService")
    private UserService userService;
    
    @Inject
    private RouterManager router;
    
    @Inject
    private SessionManager session;
    
    @Message(message="*")
    public void parseUserInput(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        String input = update.getMessage().getText();
        this.userService.setBlacklistedTrue(Integer.parseInt(input));
        deleter.setMessageId(messageId);
        deleter.setChatId(userid);
        deleter.deleteMessage();
        sender.setChatId(ApplicationContext.getCurrentUserId());
        sender.setText("Menu");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton createAlert = new InlineButton("Create Alert", "new_alert_action");
        InlineButton viewSubscriptions = new InlineButton("View Alerts", "view_alerts_action");
        InlineButton joinAlert = new InlineButton("Join Alert", "join_alert_action");
        buttons.add(createAlert);
        buttons.add(viewSubscriptions);
        buttons.add(joinAlert);
        InlineButton blacklist = new InlineButton("Blacklist", "blacklist_action");
        if(ApplicationContext.getCurrentUserId()==Main.ADMIN_ID)
            buttons.add(blacklist);
        sender.setInlineButtons(buttons);
        sender.sendMessage();
        
        router.routeCallbackToClass(userid, MainMenuState.class);
    }
    
    @Callback(command="back")
    public void processBackRequest(Update update) {
        int messageId = CallbackUtils.getMessageIdFromCallback(update);
        int userid = ApplicationContext.getCurrentUserId();

        deleter.setMessageId(messageId);
        deleter.setChatId(userid);
        deleter.deleteMessage();
        sender.setChatId(ApplicationContext.getCurrentUserId());
        sender.setText("Menu");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton createAlert = new InlineButton("Create Alert", "new_alert_action");
        InlineButton viewSubscriptions = new InlineButton("View Alerts", "view_alerts_action");
        InlineButton joinAlert = new InlineButton("Join Alert", "join_alert_action");
        buttons.add(createAlert);
        buttons.add(viewSubscriptions);
        buttons.add(joinAlert);
        InlineButton blacklist = new InlineButton("Blacklist", "blacklist_action");
        if(ApplicationContext.getCurrentUserId()==Main.ADMIN_ID)
            buttons.add(blacklist);
        sender.setInlineButtons(buttons);
        sender.sendMessage();
        
        router.routeCallbackToClass(userid, MainMenuState.class);
    }
}
