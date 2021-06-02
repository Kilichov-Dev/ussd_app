package ecma.ai.ussdapp.entity;

import ecma.ai.ussdapp.entity.enums.ActionType;
import ecma.ai.ussdapp.entity.template.AbsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Details extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

//    @OneToOne
//    private Client client;

    @ManyToOne//bitta simkartaga ko'pgina detaillar to'g'ri keladi
    private SimCard simCard;

    //ayni vaqtda qancha miqdorda ishlatilganligi
    private Float amount;
}
