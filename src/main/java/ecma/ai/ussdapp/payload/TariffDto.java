package ecma.ai.ussdapp.payload;

import ecma.ai.ussdapp.entity.enums.ClientType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class TariffDto {
    private String name;
    private double price;
    private double switchPrice;
    private int expireDate;
    private int mb;
    private int sms;
    private int min;
    private int mbCost;
    private int smsCost;
    private int minCost;
    private Integer clientTypeId;

}
