package lkt.util;

import lkt.model.AttachmentType;
import lkt.model.Role;
import lkt.model.State;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Util {
    public static AttachmentType getAttachmentTypeFromString(String string) {
        for (AttachmentType attachmentType : AttachmentType.values()) {
            if (string.equals(attachmentType.toString())) return attachmentType;
        }
        return null;
    }

    public static Role getRoleFromString(String string) {
        for (Role role : Role.values()) {
            if (string.equals(role.toString())) return role;
        }
        return null;
    }

    public static State getStateFromString(String string) {
        for (State state : State.values()) {
            if (string.equals(state.toString())) return state;
        }
        return null;
    }

    public static LocalDateTime getLocalDateTime(ResultSet resultSet, String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
