package lkt.service.eventLog;

import lkt.model.User;
import lkt.observer.INotificationSubscriber;
import lkt.repository.TicketRepository;
import lkt.util.NotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventLogService implements INotificationSubscriber {

    @Autowired
    TicketRepository ticketRepository;

    @Override
    public void update(User actionUser, List<User> receivingUserList, String action, int ticketID, String ticketTitle, String actionResult) {
        if (action.equals("comment")) return;
        String comment = NotificationUtil.buildNotificationMessage(actionUser, receivingUserList, action, ticketID, ticketTitle, actionResult);
        ticketRepository.insertComment(comment, actionUser.getUserID(), ticketID);
    }
}
