package lkt.observer;

import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationBroadcaster {
    private final List<INotificationSubscriber> subscriberList;

    @Autowired
    public NotificationBroadcaster(List<INotificationSubscriber> subscriberList) {
        this.subscriberList = subscriberList;
    }

    public void notifySubscribers(User actionUser, List<User> receivingUserList, String action, int ticketID, String ticketTitle, String actionResult) {
        subscriberList.forEach(subscriber ->
                subscriber.update(actionUser, receivingUserList, action, ticketID, ticketTitle, actionResult));
    }
}
