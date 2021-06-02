package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SimCardRepository extends JpaRepository<SimCard, UUID> {
    Optional<SimCard> findBySimCardNumber(String simCardNumber);

    Optional<SimCard> findByCodeAndNumber(String code, String number);

    boolean existsByCodeAndNumber(String code, String number);
}
