package lkt.repository;

import lkt.model.Department;
import java.util.List;

public interface IDepartmentRepository {
    List<Department> findAll();
    Department findByID(int id);
}
