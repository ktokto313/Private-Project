package lkt.controller;

import lkt.model.TicketType;
import lkt.service.ITicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ticketType")
public class TicketTypeController {
    @Autowired
    private ITicketTypeService ticketTypeService;

    @GetMapping
    public ResponseEntity<List<TicketType>> getAll() {
        return ResponseEntity.ok(ticketTypeService.getAllTicketTypes());
    }

    @PostMapping
    public ResponseEntity<TicketType> addTicketType(@RequestBody TicketType ticketType) {
        TicketType created = ticketTypeService.addTicketType(ticketType);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

