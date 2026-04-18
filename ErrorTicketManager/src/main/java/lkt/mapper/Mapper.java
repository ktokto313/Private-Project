package lkt.mapper;

import lkt.model.*;
import lkt.util.Util;
import org.postgresql.util.PGInterval;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Mapper {
    public User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserID(resultSet.getInt("id"));
        user.setRole(Util.getRoleFromString(resultSet.getString("role")));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        Department department = new Department();
        department.setID(resultSet.getInt("departmentid"));
        user.setDepartment(department);
        return user;
    }

    public Ticket mapTicket(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setID(resultSet.getInt("ticket_id"));
        ticket.setTitle(resultSet.getString("ticket_title"));
        ticket.setDetail(resultSet.getString("ticket_detail"));
        ticket.setState(Util.getStateFromString(resultSet.getString("ticket_state")));
        ticket.setTimeCreated(Util.getLocalDateTime(resultSet, "ticket_timecreated"));
        ticket.setTimeProcessing(Util.getLocalDateTime(resultSet, "ticket_timeprocessing"));
        ticket.setTimeResolved(Util.getLocalDateTime(resultSet, "ticket_timeresolved"));
        ticket.setCause(resultSet.getString("ticket_cause"));
        ticket.setCreator(mapUser(resultSet, "creator"));
        ticket.setAssignee(mapUser(resultSet, "assignee"));
        ticket.setPriority(mapTicketPriority(resultSet));
        ticket.setTicketType(mapTicketType(resultSet));
        return ticket;
    }

    public User mapUser(ResultSet resultSet, String prefix) throws SQLException {
        Integer id = (Integer) resultSet.getObject(prefix + "_id");
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setUserID(id);
        user.setRole(resultSet.getString(prefix + "_role"));
        user.setUsername(resultSet.getString(prefix + "_username"));
        user.setDepartmentID((Integer) resultSet.getObject(prefix + "_departmentid"));
        return user.getUserNoPassword();
    }

    public Priority mapTicketPriority(ResultSet resultSet) throws SQLException {
        Integer id = (Integer) resultSet.getObject("priority_id");
        if (id == null) {
            return null;
        }
        Priority priority = new Priority();
        priority.setID(id);
        priority.setLevelOfPriority(resultSet.getInt("priority_levelofpriority"));
        priority.setName(resultSet.getString("priority_name"));
        return priority;
    }

    public TicketType mapTicketType(ResultSet resultSet) throws SQLException {
        Integer id = (Integer) resultSet.getObject("tickettype_id");
        if (id == null) {
            return null;
        }
        TicketType ticketType = new TicketType();
        ticketType.setID(id);
        ticketType.setTitle(resultSet.getString("tickettype_title"));
        ticketType.setDescription(resultSet.getString("tickettype_description"));
        return ticketType;
    }

    public TicketType mapTicketTypeRow(ResultSet resultSet) throws SQLException {
        TicketType ticketType = new TicketType();
        ticketType.setID(resultSet.getInt("id"));
        ticketType.setTitle(resultSet.getString("title"));
        ticketType.setDescription(resultSet.getString("description"));
        return ticketType;
    }

    public Priority mapPriority(ResultSet resultSet) throws SQLException {
        Priority priority = new Priority();
        priority.setID(resultSet.getInt("id"));
        priority.setLevelOfPriority(resultSet.getInt("levelofpriority"));
        priority.setName(resultSet.getString("name"));
        priority.setTimeToRespond(resultSet.getObject("timetorespond", PGInterval.class));
        priority.setTimeToFinish(resultSet.getObject("timetofinish", PGInterval.class));
        return priority;
    }

    public Department mapDepartmentRow(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setID(resultSet.getInt("id"));
        department.setName(resultSet.getString("name"));
        return department;
    }
}
