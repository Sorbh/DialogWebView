package in.unicodelabs.view.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by saurabh on 4/3/18.
 */

public class HexColorValidator {
    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";


    public static boolean validate(final String hexColorCode) {
        Pattern  pattern = Pattern.compile(HEX_PATTERN);
        Matcher matcher = pattern.matcher(hexColorCode);
        return matcher.matches();

    }
}
