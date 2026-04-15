package lkt.controller;

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
import lkt.model.Priority;
import lkt.service.IAdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @PutMapping("/users/{userID}/password")
    public ResponseEntity<Void> changeUserPassword(
            @PathVariable Integer userID,
            @RequestParam String newPassword
    ) {
        boolean updated = adminService.changeUserPassword(userID, newPassword);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userID}/department")
    public ResponseEntity<Void> changeDepartment(
            @PathVariable Integer userID,
            @RequestParam Integer department
    ) {
        boolean updated = adminService.changeDepartment(userID, department);
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
            @RequestParam Priority priority
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
}
