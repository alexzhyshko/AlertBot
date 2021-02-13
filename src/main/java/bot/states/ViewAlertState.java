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
import application.context.annotation.State;
import application.routing.RouterManager;
import application.session.SessionManager;
import bot.Main;
import bot.model.Alert;
import bot.model.User;
import bot.service.AlertService;
import bot.service.SubscriptionService;
import bot.util.CallbackUtils;

@Component
@State(3)
public class ViewAlertState {

    @Inject
    private MessageSender sender;
    
    @Inject
    private MessageEditor editor;

    @Inject
    private MessageDeleter deleter;
    
    @Inject
    private SessionManager session;

    @Inject
    private RouterManager router;
    
    @Inject("defaultAlertService")
    private AlertService alertService;
    
    @Inject("defaultSubscriptionService")
    private SubscriptionService subscriptionService;
    
    @Callback(command="*")
    public void processViewAlertRewuest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        
        String command = update.getCallbackQuery().getData();
        String alertName = command.substring(18);
        Alert alert = alertService.getAlertByName(alertName);
        int ownerid = alert.getOwner().getUserId();
        
        this.session.setProperty("current_chosen_alert_name", alertName);
        
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Name: "+alertName);
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton delete = new InlineButton("Delete", "alert_delete");
        InlineButton send = new InlineButton("SEND", "alert_send");
        InlineButton leave = new InlineButton("Leave", "alert_leave");
        InlineButton back = new InlineButton("Menu", "back");
        if(userid==ownerid)
            buttons.add(delete);
        buttons.add(send);
        if(userid!=ownerid)
            buttons.add(leave);
        buttons.add(back);
        editor.setInlineButtons(buttons);
        editor.editMessage();
    }
    
    @Callback(command="alert_delete")
    public void processDeleteAlertRequest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        String alertName = this.session.getProperty("current_chosen_alert_name", String.class);
        this.session.setProperty("current_chosen_alert_name", null);
        
        alertService.deleteAlert(alertName);
        
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
    
    @Callback(command="alert_send")
    public void processSendAlertRequest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        String alertName = this.session.getProperty("current_chosen_alert_name", String.class);
        this.session.setProperty("current_chosen_alert_name", null);
        Alert alert = alertService.getAlertByName(alertName);
        List<User> users = alertService.getAllUsersByAlert(alertName);
        for(User user : users) {
            sender.setChatId(user.getUserId());
            sender.setText("🔔🔔🔔🔔🔔\n\n"+alert.getName()+"\n\n"+alert.getMessage());
            if(user.getUserId()==Main.ADMIN_ID || user.getUserId() == alert.getOwner().getUserId()) {
                List<InlineButton> buttons = new ArrayList<>();
                InlineButton createAlert = new InlineButton(Integer.toString(userid), "fake");
                buttons.add(createAlert);
                sender.setInlineButtons(buttons);
            }
            sender.sendMessage();
        }
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
    
    @Callback(command="alert_leave")
    public void processLeaveAlertRequest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        String alertName = this.session.getProperty("current_chosen_alert_name", String.class);
        this.session.setProperty("current_chosen_alert_name", null);
        
        subscriptionService.unsubscribeUserFromAlert(userid, alertName);
        
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
