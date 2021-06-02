package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {
    boolean existsByName(String name);

}
