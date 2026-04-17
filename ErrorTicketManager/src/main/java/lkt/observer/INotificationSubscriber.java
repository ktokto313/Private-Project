package lkt.observer;

import lkt.model.User;

import java.util.List;

public interface INotificationSubscriber {
    void update(User actionUser, List<User> receivingUserList, String action, int ticketID, String ticketTitle, String actionResult);
}
