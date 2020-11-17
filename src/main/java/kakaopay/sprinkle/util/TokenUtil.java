package kakaopay.sprinkle.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class TokenUtil {

    public String generate() {
        return RandomStringUtils.randomAlphanumeric(3);
    }

}
