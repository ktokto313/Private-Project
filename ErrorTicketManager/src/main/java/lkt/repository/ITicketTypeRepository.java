package lkt.repository;

import lkt.model.TicketType;

import java.util.List;

public interface ITicketTypeRepository {
    List<TicketType> findAll();

    TicketType findByID(int id);

    TicketType insert(TicketType ticketType);
}

