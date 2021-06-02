package ecma.ai.ussdapp.entity;

import ecma.ai.ussdapp.entity.template.AbsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class EntertainingService extends AbsEntity {

    private String name;

    private double price;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    private ServiceCategory serviceCategory;

    private Timestamp expiredDate;
    @ManyToOne
    private SimCard simCard;
    private Integer count;

}
