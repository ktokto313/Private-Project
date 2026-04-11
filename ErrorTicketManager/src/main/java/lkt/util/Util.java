package lkt.util;

import lkt.model.AttachmentType;
import lkt.model.Role;
import lkt.model.State;

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
}
