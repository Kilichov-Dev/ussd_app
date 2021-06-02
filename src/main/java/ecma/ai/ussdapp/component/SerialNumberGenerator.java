package ecma.ai.ussdapp.component;

import java.util.Locale;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.String.format;

public class SerialNumberGenerator {

    private final Random GENERATOR = new Random();

    private final String[] IMEI_CODE_ALL = {"01", "10", "30", "33", "35", "44",
            "45", "49", "50", "51", "52", "53", "54", "86", "91", "98", "99"};

//    public String generatorImeiCode() {
//        String bir = format("%s%.12", IMEI_CODE_ALL[GENERATOR.nextInt(IMEI_CODE_ALL.length)],
//                format(Locale.ENGLISH, "%012d", abs(GENERATOR.nextLong())));
//
//        int sum = 0;
//        for (int i = 0; i < bir.length(); i++) {
//            int c = Character.digit(bir.charAt(i), 10);
//            sum += (i % 2 == 0 ? c : sumDigits(c * 2));
//        }
//        int finalDigit = (10 - (sum % 10)) % 10;
//        return bir + finalDigit;
//    }

    public int sumDigits(int number) {
        int a = 0;
        while (number > 0) {
            a = a + number % 10;
            number = number / 10;
        }
        return a;
    }
}
