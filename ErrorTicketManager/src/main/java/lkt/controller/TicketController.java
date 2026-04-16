package lkt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lkt.model.Ticket;
import lkt.model.User;
import lkt.service.ITicketService;
import lkt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private ITicketService ticketService;

    @PostMapping
    public ResponseEntity<Void> createTicket(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "priority") Integer priorityID,
            @RequestParam(value = "type", required = false) Integer typeID,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        User authenticatedUser = getUser(request);
        boolean created = ticketService.createTicket(
                title,
                description,
                priorityID,
                typeID,
                getFileBytes(file),
                getFileContentType(file),
                authenticatedUser
        );
        if (!created) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets(HttpServletRequest request) {
        User authenticatedUser = getUser(request);
        return ResponseEntity.ok(ticketService.getTickets(authenticatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> viewTicket(
            @PathVariable("id") Integer ticketID,
            HttpServletRequest request
    ) {
        User authenticatedUser = getUser(request);
        Ticket ticket = ticketService.viewTicket(ticketID, authenticatedUser);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTicket(
            @PathVariable("id") Integer ticketID,
            @RequestParam Ticket ticket,
            HttpServletRequest request
    ) {
        User authenticatedUser = getUser(request);
        boolean updated = ticketService.updateTicket(ticketID, ticket, authenticatedUser);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/assignees")
    public ResponseEntity<Void> addAssignee(
            @PathVariable("id") Integer ticketID,
            @RequestParam Integer userID,
            HttpServletRequest request
    ) {
        User authenticatedUser = getUser(request);
        boolean assigned = ticketService.addAssignee(ticketID, userID, authenticatedUser);
        if (!assigned) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable("id") Integer ticketID,
            @RequestParam(value = "detail", required = false) String detail,
            @RequestParam(value = "attachedFile", required = false) MultipartFile attachedFile,
            HttpServletRequest request
    ) throws IOException {
        User authenticatedUser = getUser(request);
        boolean created = ticketService.addComment(
                ticketID,
                detail,
                getFileBytes(attachedFile),
                getFileContentType(attachedFile),
                authenticatedUser
        );
        if (!created) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private User getUser(HttpServletRequest request) {
        return JWTUtil.getUser(request);
    }

    private byte[] getFileBytes(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return file.getBytes();
    }

    private String getFileContentType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return file.getContentType();
    }
}
