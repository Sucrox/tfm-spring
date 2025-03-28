package es.upm.miw.companyds.tfm_spring.api.dto.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    public static final String VALID_PHONE = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    public static final String VALID_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";


    private Validations() {
        //Empty
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(VALID_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

