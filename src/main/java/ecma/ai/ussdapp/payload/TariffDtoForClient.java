package ecma.ai.ussdapp.payload;

import lombok.Data;

@Data
public class TariffDtoForClient {
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
}
