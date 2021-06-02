package ecma.ai.ussdapp.component;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class NumberGenerator {

    public String generetorNumber(int number) {
        final String val = "1234567890";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < number; i++) {
            int i1 = random.nextInt(val.length());
            stringBuilder.append(val.charAt(i1));
        }
        return stringBuilder.toString();

    }
}
