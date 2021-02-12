package bot.states;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.objects.Update;

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
import bot.model.Alert;
import bot.service.AlertService;
import bot.util.CallbackUtils;

@Component
@State(1)
public class MainMenuState {

    @Inject
    private MessageSender sender;
    
    @Inject
    private MessageEditor editor;

    @Inject
    private SessionManager session;

    @Inject
    private RouterManager router;
    
    @Inject("defaultAlertService")
    private AlertService alertService;
    
    @Message(message = "*")
    public void outputInitialMenu(Update update) {
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
    }

    @Callback(command = "new_alert_action")
    public void processNewAlertRequest(Update update) {
        int messageId = CallbackUtils.getMessageIdFromCallback(update);
        int userid = ApplicationContext.getCurrentUserId();
        
        this.session.setProperty("mainMessageId", messageId);
        router.routeCallbackToClass(userid, AddNewAlertState.class);
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Name: Not given\nMessage: Not given\n\nUse buttons below to choose what to input");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton setName = new InlineButton("Name", "new_alert_set_name");
        InlineButton setMessage = new InlineButton("Message", "new_alert_set_message");
        InlineButton back = new InlineButton("Back", "back");
        buttons.add(setName);
        buttons.add(setMessage);
        buttons.add(back);
        editor.setInlineButtons(buttons);
        editor.editMessage();
        
        this.session.setProperty("create_alert_setting_name", false);
        this.session.setProperty("create_alert_setting_message", false);
        
        
    }

    @Callback(command = "view_alerts_action")
    public void processViewAlertsRequest(Update update) {
        int messageId = CallbackUtils.getMessageIdFromCallback(update);
        int userid = ApplicationContext.getCurrentUserId();
        
        this.session.setProperty("mainMessageId", messageId);
        
        List<Alert> alerts = alertService.getAllUserAlerts(userid);
        InlineButton back = new InlineButton("Back", "back");
        if(!alerts.isEmpty()) {
            List<InlineButton> buttons = alerts.stream().map(alert->new InlineButton(alert.getName(), "alert_choice_view_"+alert.getName())).collect(Collectors.toList());
            buttons.add(back);
            editor.setChatId(userid);
            editor.setMessageId(messageId);
            editor.setInlineButtons(buttons);
            editor.setText("Choose alert to view");
            editor.editMessage();
            router.routeCallbackToClass(userid, ViewAlertState.class);
        }else {
            sender.setChatId(userid);
            sender.setText("No alerts, go subscribe");
            sender.sendMessage();
        }
    }
    
    @Callback(command = "join_alert_action")
    public void processJoinAlertRequest(Update update) {
        int messageId = CallbackUtils.getMessageIdFromCallback(update);
        int userid = ApplicationContext.getCurrentUserId();

        this.session.setProperty("mainMessageId", messageId);
        
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Input alert name");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton back = new InlineButton("Back", "back");
        buttons.add(back);
        editor.setInlineButtons(buttons);
        editor.editMessage();
        
        router.routeCallbackToClass(userid, JoinAlertState.class);
    }
    
    @Callback(command = "blacklist_action")
    public void processBlacklistRequest(Update update) {
        int messageId = CallbackUtils.getMessageIdFromCallback(update);
        int userid = ApplicationContext.getCurrentUserId();

        this.session.setProperty("mainMessageId", messageId);
        
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Input id");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton back = new InlineButton("Back", "back");
        buttons.add(back);
        editor.setInlineButtons(buttons);
        editor.editMessage();
        
        router.routeCallbackToClass(userid, BlacklistState.class);
    }
    
   

}
