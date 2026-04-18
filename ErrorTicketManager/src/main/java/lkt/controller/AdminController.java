package lkt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lkt.model.*;
import lkt.util.JWTUtil;
import lkt.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lkt.service.IAdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<User> addAccount(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            User created = adminService.addAccount(username, password);

            if (created == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(created.getUserNoPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAccounts(
            @RequestParam(value="role", required = false) String string
    ) {
        Role role = Util.getRoleFromString(string);
        List<User> userList = adminService.getAccountsByRole(role);
        if (userList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/")
    public ResponseEntity<User> getAccount(@RequestParam String username) {
        User foundUser = adminService.getAccount(username);
        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foundUser);
    }

    @PutMapping("/users/{userID}/password")
    public ResponseEntity<Void> changeUserPassword(
            @PathVariable Integer userID,
            @RequestBody User user
    ) {
        if (user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean updated = adminService.changeUserPassword(userID, user.getPassword());
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        List<Department> departmentList = adminService.getDepartments();
        if (departmentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(departmentList);
    }

    @PutMapping("/users/{userID}/department")
    public ResponseEntity<Void> changeDepartment(
            @PathVariable Integer userID,
            @RequestBody Department department
    ) {
        boolean updated = adminService.changeDepartment(userID, department.getID());
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userID}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer userID) {
        boolean deleted = adminService.deleteAccount(userID);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<Priority>> getPriorities() {
        return ResponseEntity.ok(adminService.getPriorities());
    }

    @PostMapping("/priorities")
    public ResponseEntity<Priority> createPriority(@RequestBody Priority priority) {
        Priority created = adminService.createPriority(priority);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/priorities/{priority-id}")
    public ResponseEntity<Void> changePriority(
            @PathVariable("priority-id") Integer priorityID,
            @RequestBody Priority priority
    ) {
        boolean updated = adminService.changePriority(priority);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/priorities/{priority-id}")
    public ResponseEntity<Void> deletePriority(@PathVariable("priority-id") Integer priorityID) {
        boolean deleted = adminService.deletePriority(priorityID);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tickets/{ticket-id}")
    public ResponseEntity<Void> updateTicket(
            @PathVariable("ticket-id") Integer ticketID,
            @RequestBody Ticket ticket,
            @RequestParam(value="state", required = false) String string,
            HttpServletRequest request) {
        User currentUser = JWTUtil.getUser(request);
        ticket.setState(Util.getStateFromString(string));
        boolean updated = adminService.updateTicket(ticketID, ticket, currentUser);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.noContent().build();
    }
}
