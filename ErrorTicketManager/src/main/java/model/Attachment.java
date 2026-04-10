package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;

import java.util.UUID;

@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "attached_object_type", nullable = false)
    private AttachmentType attachedObjectType;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    //TODO fix this if bug / any mapping for n-way rel
    @Any
    @AnyKeyJavaClass(UUID.class) //the foreign key type
    @AnyDiscriminatorValue(discriminator = "TICKET",    entity = Ticket.class)
    @AnyDiscriminatorValue(discriminator = "COMMENT", entity = Comment.class)
    @Column(name = "attachable_type")               // the discriminator column
    @JoinColumn(name = "attachable_id")             // the FK column
    private Object attachable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader", nullable = false)
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
