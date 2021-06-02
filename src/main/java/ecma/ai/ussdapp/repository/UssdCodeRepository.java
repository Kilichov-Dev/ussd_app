package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.UssdCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UssdCodeRepository extends JpaRepository<UssdCode, Integer> {
    boolean existsByCode(String code);
}
