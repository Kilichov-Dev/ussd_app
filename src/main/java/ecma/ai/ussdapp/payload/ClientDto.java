package ecma.ai.ussdapp.payload;

import lombok.Data;

import java.util.List;

@Data
public class ClientDto {

private String passportNumber;
private String fullName;
private Integer clientTypeOrdinal;
private List<BuySimCardDto> buySimCardDtos;
}
