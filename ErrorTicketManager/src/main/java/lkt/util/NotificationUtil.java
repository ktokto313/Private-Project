package lkt.util;

import lkt.model.State;
import lkt.model.User;

import java.util.List;

public class NotificationUtil {
    public static String buildNotificationMessage(User actionUser, List<User> receivingUserList, String action, int ticketID, String ticketTitle, String actionResult) {
        String actionText = "";
        State state = Util.getStateFromString(action);
        if (state != null) {
            switch (state) {
                case CREATED -> {
                    actionText = "created a new";
                }
                case PROCESSING -> {
                    actionText = "changed state of the";
                    actionResult = State.PROCESSING.name();
                }
                case RESOLVED -> {
                    actionText = "mark";
                    actionResult = "as resolved";
                }
                case DONE -> {
                    actionText = "accepted resolution of";
                }
            }
        } else {
            actionText = switch (action) {
                case "assignee" -> "changed assignee of";
                case "ticketType" -> "changed ticket type of";
                case "priority" -> "changed priority of";
                case "comment" -> "add a comment to";
                default -> actionText;
            };
        }
        StringBuilder sb = new StringBuilder(String.format("%s %s %s ticket with title %s", actionUser.getRole(),
                actionUser.getUsername(), actionText, ticketTitle));
        if (actionResult != null) {
            sb.append(" into ");
            sb.append(actionResult);
        }
        return sb.toString();
    }
}
