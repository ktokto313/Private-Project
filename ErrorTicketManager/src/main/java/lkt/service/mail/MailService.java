package lkt.service.mail;

import lkt.model.User;
import lkt.observer.INotificationSubscriber;
import lkt.util.NotificationUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailService implements INotificationSubscriber {

    @Override
    public void update(User actionUser, List<User> receivingUserList, String action, int ticketID, String ticketTitle, String actionResult) {
        for (User user : receivingUserList) {
            String message = NotificationUtil.buildNotificationMessage(actionUser, receivingUserList, action, ticketID, ticketTitle, actionResult);
            System.out.printf("Sending mail from %s@gmail.com to %s@gmail.com with content %s\n", "ticketSystem", user.getUsername(), message);
        }
    }
}
