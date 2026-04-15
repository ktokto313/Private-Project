package lkt.service;

import lkt.model.Priority;
import lkt.repository.IPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService implements IPriorityService {
    @Autowired
    private IPriorityRepository priorityRepository;

    @Override
    public List<Priority> getAllPriorities() {
        return priorityRepository.findAll();
    }
}

