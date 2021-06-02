package ecma.ai.ussdapp.payload;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class SimCardDto {
    private String name;
    private String code;

}
