package ecma.ai.ussdapp.payload;

import ecma.ai.ussdapp.entity.ServiceCategory;
import ecma.ai.ussdapp.entity.SimCard;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
public class ServiceDto {

    private String name;

    private double price;

    private Integer serviceCategoryId;

    private Timestamp expiredDate;
    private SimCard simCard;
}
