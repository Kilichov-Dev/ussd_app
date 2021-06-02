package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.Details;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DetailsRepository extends JpaRepository<Details, UUID> {
    List<Details> findAllBySimCard(SimCard simCard);
    List<Details> findAllByActionTypeAndSimCard(ActionType actionType, SimCard simCard);
}
