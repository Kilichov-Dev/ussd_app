package ecma.ai.ussdapp.payload;

import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private SimCard simCard; //o'zgarishi mumkin
    private Float amount;
}
