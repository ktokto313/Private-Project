package lkt.service;

import lkt.model.Priority;
import lkt.repository.IPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService implements IPriorityService {
    private IPriorityRepository priorityRepository;

    @Autowired
    public PriorityService(IPriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public List<Priority> getAllPriorities() {
        return priorityRepository.findAll();
    }
}

