package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.IAdminService;

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
}
