package ecma.ai.ussdapp.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BuySimCardDto {

private String code;
private String number;
private Double sum;
private UUID tariffId;

}
