package lkt.repository;

import lkt.mapper.Mapper;
import lkt.model.Attachment;
import lkt.model.AttachmentType;
import lkt.model.Comment;
import lkt.model.State;
import lkt.model.Ticket;
import lkt.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepository implements ITicketRepository {
    @Autowired
    private Connection connection;

    @Autowired
    private Mapper mapper;

    @Override
    public Integer insertTicket(
            String title,
            String description,
            Integer creatorID,
            Integer priorityID,
            Integer ticketTypeID
    ) {
        String sql = """
                insert into tickets (title, detail, creator, state, priority, tickettype, timecreated)
                values (?, ?, ?, ?::state, ?, ?, ?)
                returning id
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, creatorID);
            preparedStatement.setString(4, State.CREATED.toString());
            preparedStatement.setInt(5, priorityID);
            if (ticketTypeID == null) {
                preparedStatement.setNull(6, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(6, ticketTypeID);
            }
            preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertAttachment(byte[] content, String contentType, String attachedObjectType, Integer attachedObjectID) {
        String sql = """
                insert into attachments (content, attachedobjecttype, contenttype, attachedobjectid)
                values (?, ?::attachmenttype, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBytes(1, content);
            preparedStatement.setString(2, attachedObjectType);
            preparedStatement.setString(3, contentType);
            preparedStatement.setInt(4, attachedObjectID);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Ticket> findAccessibleTickets(Integer userID, boolean includeAllTickets) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = baseTicketQuery() + """
                where (? = true or t.creator = ? or t.assignee = ?)
                order by t.timecreated desc, t.id desc
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, includeAllTickets);
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(mapper.mapTicket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Ticket findAccessibleTicketByID(Integer ticketID, Integer userID, boolean includeAllTickets) {
        String sql = baseTicketQuery() + """
                where t.id = ? and (? = true or t.creator = ? or t.assignee = ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ticketID);
            preparedStatement.setBoolean(2, includeAllTickets);
            preparedStatement.setInt(3, userID);
            preparedStatement.setInt(4, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            Ticket ticket = mapper.mapTicket(resultSet);
            ticket.setAttachments(findAttachments(AttachmentType.TICKET, ticketID));
            ticket.setComments(findComments(ticketID));
            return ticket;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateTicket(Integer ticketID, Ticket ticket) {
        String sql = """
                update tickets
                set state = ?::state,
                    timeprocessing = case when ? = 'PROCESSING' then coalesce(timeprocessing, ?) else timeprocessing end,
                    timeresolved = case when ? in ('RESOLVED', 'DONE') then ? else timeresolved end,
                    assignee = ?,
                    priority = ?
                where id = ?
                """;
        LocalDateTime now = LocalDateTime.now();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String ticketState = ticket.getState().toString();
            preparedStatement.setString(1, ticketState);
            preparedStatement.setString(2, ticketState);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(now));
            preparedStatement.setString(4, ticketState);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(now));
            preparedStatement.setInt(6, ticket.getAssignee().getUserID());
            preparedStatement.setInt(7, ticket.getPriority().getID());
            preparedStatement.setInt(8, ticketID);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAssignee(Integer ticketID, Integer userID) {
        String sql = "update tickets set assignee = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, ticketID);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer insertComment(String detail, Integer creatorID, Integer ticketID) {
        String sql = """
                insert into comments (detail, creator, ticketid, timecreated)
                values (?, ?, ?, ?)
                returning id
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, detail);
            preparedStatement.setInt(2, creatorID);
            preparedStatement.setInt(3, ticketID);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Comment> findComments(Integer ticketID) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = """
                select c.id as comment_id,
                       c.detail as comment_detail,
                       c.timecreated as comment_timecreated,
                       cu.id as comment_creator_id,
                       cu.role as comment_creator_role,
                       cu.username as comment_creator_username,
                       cu.departmentid as comment_creator_departmentid
                from comments c
                left join users cu on cu.id = c.creator
                where c.ticketid = ?
                order by c.timecreated desc, c.id desc 
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ticketID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setID(resultSet.getInt("comment_id"));
                comment.setDetail(resultSet.getString("comment_detail"));
                comment.setTimeCreated(Util.getLocalDateTime(resultSet, "comment_timecreated"));
                comment.setCreator(mapper.mapUser(resultSet, "comment_creator"));
                comment.setAttachments(findAttachments(AttachmentType.COMMENT, comment.getID()));
                comments.add(comment);
            }
        }
        return comments;
    }

    private List<Attachment> findAttachments(AttachmentType attachmentType, Integer attachedObjectID) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        String sql = """
                select id, content, attachedobjecttype, contenttype, attachedobjectid
                from attachments
                where attachedobjecttype = ?::attachmenttype and attachedobjectid = ?
                order by id asc
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, attachmentType.toString());
            preparedStatement.setInt(2, attachedObjectID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(resultSet.getInt("id"));
                attachment.setContent(resultSet.getBytes("content"));
                attachment.setAttachedObjectType(Util.getAttachmentTypeFromString(resultSet.getString("attachedobjecttype")));
                attachment.setAttachedObjectID(resultSet.getInt("attachedobjectid"));
                attachment.setContentType(resultSet.getString("contenttype"));
                attachments.add(attachment);
            }
        }
        return attachments;
    }

    private String baseTicketQuery() {
        return """
                select t.id as ticket_id,
                       t.title as ticket_title,
                       t.detail as ticket_detail,
                       t.state as ticket_state,
                       t.timecreated as ticket_timecreated,
                       t.timeprocessing as ticket_timeprocessing,
                       t.timeresolved as ticket_timeresolved,
                       t.cause as ticket_cause,
                       c.id as creator_id,
                       c.role as creator_role,
                       c.username as creator_username,
                       c.departmentid as creator_departmentid,
                       a.id as assignee_id,
                       a.role as assignee_role,
                       a.username as assignee_username,
                       a.departmentid as assignee_departmentid,
                       p.id as priority_id,
                       p.levelofpriority as priority_levelofpriority,
                       p.name as priority_name,
                       tt.id as tickettype_id,
                       tt.title as tickettype_title,
                       tt.description as tickettype_description
                from tickets t
                left join users c on c.id = t.creator
                left join users a on a.id = t.assignee
                left join priorities p on p.id = t.priority
                left join tickettypes tt on tt.id = t.tickettype
                """;
    }
}
