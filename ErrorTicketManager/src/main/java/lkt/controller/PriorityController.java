package lkt.controller;

import lkt.model.Priority;
import lkt.service.IPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {
    @Autowired
    private IPriorityService priorityService;

    @GetMapping
    public ResponseEntity<List<Priority>> getAll() {
        return ResponseEntity.ok(priorityService.getAllPriorities());
    }
}

