package lkt.service;

import lkt.model.TicketType;
import lkt.repository.ITicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketTypeService implements ITicketTypeService {
    @Autowired
    private ITicketTypeRepository ticketTypeRepository;

    @Override
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    @Override
    public TicketType addTicketType(TicketType ticketType) {
        if (ticketType == null) {
            return null;
        }
        if (ticketType.getTitle() == null || ticketType.getTitle().isBlank()) {
            return null;
        }
        if (ticketType.getDescription() == null || ticketType.getDescription().isBlank()) {
            return null;
        }
        ticketType.setTitle(ticketType.getTitle().trim());
        ticketType.setDescription(ticketType.getDescription().trim());
        return ticketTypeRepository.insert(ticketType);
    }
}

