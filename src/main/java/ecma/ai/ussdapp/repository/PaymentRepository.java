package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.Payment;
import ecma.ai.ussdapp.entity.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findAllBySimCardNumber(String simCard_number);
}
