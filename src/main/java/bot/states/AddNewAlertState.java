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
import bot.service.AlertService;
import bot.util.CallbackUtils;

@Component
@State(2)
public class AddNewAlertState {

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
    
    @Message(message="*")
    public void parseInput(Update update) {
        boolean settingName =  this.session.getProperty("create_alert_setting_name", Boolean.class);
        boolean settingMessage =  this.session.getProperty("create_alert_setting_message", Boolean.class);
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        
        String input = update.getMessage().getText();
        
        if(settingName) {
            this.session.setProperty("new_alert_name", input);
        } else if(settingMessage) {
            this.session.setProperty("new_alert_message", input);
        }
        this.session.setProperty("create_alert_setting_name", false);
        this.session.setProperty("create_alert_setting_message", false);
        
        String alertName = "Not given";
        boolean nameIsSet = false;
        try {
            alertName = this.session.getProperty("new_alert_name", String.class);
            nameIsSet = true;
        } catch(NullPointerException e) {
            
        }
        
        String alertMessage = "Not given";
        boolean messageIsSet = false;
        try {
            alertMessage = this.session.getProperty("new_alert_message", String.class);
            messageIsSet = true;
        } catch(NullPointerException e) {
            
        }
        
        
        editor.setChatId(userid);
        editor.setMessageId(messageId);
        editor.setText("Name: "+alertName+"\nMessage: "+alertMessage+"\n\nUse buttons below to choose what to input");
        List<InlineButton> buttons = new ArrayList<>();
        InlineButton setName = new InlineButton("Name", "new_alert_set_name");
        InlineButton setMessage = new InlineButton("Message", "new_alert_set_message");
        buttons.add(setName);
        buttons.add(setMessage);
        if(nameIsSet && messageIsSet) {
            InlineButton finish = new InlineButton("Finish", "new_alert_finish");
            buttons.add(finish);
        }
        InlineButton back = new InlineButton("Back", "back");
        buttons.add(back);
        editor.setInlineButtons(buttons);
        editor.editMessage();
    }
    
    @Callback(command="new_alert_set_name")
    public void processSetNameRequest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
       
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Name: Not given\nMessage: Not given\n\nWaiting for our input (SETTING NAME)");
        editor.editMessage();
        this.session.setProperty("create_alert_setting_name", true);
        this.session.setProperty("create_alert_setting_message", false);
    }
    
    
    @Callback(command="new_alert_set_message")
    public void processSetMessageRequest(Update update) {
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
       
        editor.setMessageId(messageId);
        editor.setChatId(userid);
        editor.setText("Name: Not given\nMessage: Not given\n\nWaiting for our input (SETTING MESSAGE)");
        editor.editMessage();
        this.session.setProperty("create_alert_setting_name", false);
        this.session.setProperty("create_alert_setting_message", true);
    }
    
    @Callback(command="new_alert_finish")
    public void processFinishRequest(Update update) {
        this.session.setProperty("create_alert_setting_name", false);
        this.session.setProperty("create_alert_setting_message", false);
        this.session.setProperty("new_alert_set_name", null);
        this.session.setProperty("new_alert_set_message", null);
       
       
        int userid = ApplicationContext.getCurrentUserId();
        int messageId = this.session.getProperty("mainMessageId", Integer.class);
        String alertName = this.session.getProperty("new_alert_name", String.class); 
        String alertMessage = this.session.getProperty("new_alert_message", String.class);

        alertService.createAlert(alertName, alertMessage, userid);
        
        
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
        sender.setInlineButtons(buttons);
        sender.sendMessage();
        
        router.routeCallbackToClass(userid, MainMenuState.class);
    }
    
    
    @Callback(command="back")
    public void processBackRequest(Update update) {
        this.session.setProperty("create_alert_setting_name", false);
        this.session.setProperty("create_alert_setting_message", false);
        this.session.setProperty("new_alert_set_name", null);
        this.session.setProperty("new_alert_set_message", null);
        
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
        sender.setInlineButtons(buttons);
        sender.sendMessage();
        
        router.routeCallbackToClass(userid, MainMenuState.class);
    }
    
}
