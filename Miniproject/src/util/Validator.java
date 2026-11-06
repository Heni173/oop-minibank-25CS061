package util;

import java.util.regex.Pattern;

// Utility class for validating mobile numbers, email addresses, PAN and IFSC codes.
public class Validator
{
    // Defines the pattern for a valid 10-digit Indian mobile number.
    private static final Pattern MOBILE =
        Pattern.compile("^[6-9][0-9]{9}$");

    // Defines the pattern for a valid email address.
    private static final Pattern EMAIL =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Defines the pattern for a valid PAN number.
    private static final Pattern PAN =
        Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    // Defines the pattern for a valid IFSC code.
    private static final Pattern IFSC =
        Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    // Checks whether the given mobile number is valid.
    public static boolean isValidMobile(String mobile)
    {
        return MOBILE.matcher(mobile).matches();
    }

    // Checks whether the given email address is valid.
    public static boolean isValidEmail(String email)
    {
        return EMAIL.matcher(email).matches();
    }

    // Checks whether the given PAN number is valid.
    public static boolean isValidPan(String pan)
    {
        return PAN.matcher(pan).matches();
    }

    // Checks whether the given IFSC code is valid.
    public static boolean isValidIfsc(String ifsc)
    {
        return IFSC.matcher(ifsc).matches();
    }
}