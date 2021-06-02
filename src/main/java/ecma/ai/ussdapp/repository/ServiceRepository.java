package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.EntertainingService;

//import jdk.nashorn.internal.runtime.options.Option;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<EntertainingService, UUID> {


    boolean existsByName(String name);

    Optional<EntertainingService> findByName(String name);
}
