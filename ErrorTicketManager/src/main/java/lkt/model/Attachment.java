package lkt.model;

public class Attachment {
    private Integer id;
    private byte[] content;
    private AttachmentType attachedObjectType;
    private Integer attachedObjectID;
    private String contentType;
    private User uploader;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public AttachmentType getAttachedObjectType() {
        return attachedObjectType;
    }

    public void setAttachedObjectType(AttachmentType attachedObjectType) {
        this.attachedObjectType = attachedObjectType;
    }

    public Integer getAttachedObjectID() {
        return attachedObjectID;
    }

    public void setAttachedObjectID(Integer attachedObjectID) {
        this.attachedObjectID = attachedObjectID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }
}
