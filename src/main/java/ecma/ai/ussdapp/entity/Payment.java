package ecma.ai.ussdapp.entity;

import ecma.ai.ussdapp.entity.enums.PayType;
import ecma.ai.ussdapp.entity.template.AbsEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
@Data
public class Payment extends AbsEntity {

    @OneToOne
    private SimCard simCard;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    private double amount;

    private String payerName;

    private String payerId;
    //qo'lda chek nomeri

}
