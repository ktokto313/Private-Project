package lkt.model;

public class Attachment {
    private Integer id;
    private AttachmentType attachedObjectType;
    private String contentType;
    private Object attachable;
    private User uploader;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttachmentType getAttachedObjectType() {
        return attachedObjectType;
    }

    public void setAttachedObjectType(AttachmentType attachedObjectType) {
        this.attachedObjectType = attachedObjectType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getAttachable() {
        return attachable;
    }

    public void setAttachable(Object attachable) {
        this.attachable = attachable;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }
}
