package ecma.ai.ussdapp.payload;

import ecma.ai.ussdapp.entity.enums.PacketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketDtoForClinets {
    private PacketType packetType;
    private String name;
    private int amount;
    private int cost;
    private int duration;
    private boolean isTariff;
    private List<String> availableTariffs;
}
