package lkt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lkt.model.State;
import lkt.model.Ticket;
import lkt.model.User;
import lkt.service.ITicketService;
import lkt.util.JWTUtil;
import lkt.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam(value="state") String string,
            HttpServletRequest request
    ) {
        State state = Util.getStateFromString(string);
        User authenticatedUser = getUser(request);
        boolean updated = ticketService.updateTicketState(ticketID, state, authenticatedUser);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

// TODO disabled api line, migrated to admin APIs
//    @PostMapping("/{id}/assignees")
//    public ResponseEntity<Void> addAssignee(
//            @PathVariable("id") Integer ticketID,
//            @RequestParam Integer userID,
//            HttpServletRequest request
//    ) {
//        User authenticatedUser = getUser(request);
//        boolean assigned = ticketService.addAssignee(ticketID, userID, authenticatedUser);
//        if (!assigned) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable("id") Integer ticketID,
            @RequestParam(value = "detail") String detail,
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
