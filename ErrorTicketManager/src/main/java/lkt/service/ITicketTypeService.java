package lkt.service;

import lkt.model.TicketType;

import java.util.List;

public interface ITicketTypeService {
    List<TicketType> getAllTicketTypes();

    TicketType addTicketType(TicketType data);
}

