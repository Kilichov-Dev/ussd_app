package ecma.ai.ussdapp.payload;

import lombok.Data;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;

@Data
public class CallDto {
    private String code;
    private String number;
    private double seconds;


}
