// Program: Password strength checker using regex and String methods

public class PasswordChecker
{
    // Rule 1: length must be at least 8
    public static boolean hasLength(String pw)
    {
        return pw.length() >= 8;
    }

    // Rule 2: must contain an uppercase letter
    public static boolean hasUpperCase(String pw)
    {
        return pw.matches(".*[A-Z].*");
    }

    // Rule 3: must contain a digit
    public static boolean hasDigit(String pw)
    {
        return pw.matches(".*[0-9].*");
    }

    // Rule 4: must contain a special character (anything except letters and digits)
    public static boolean hasSpecialChar(String pw)
    {
        return pw.matches(".*[^A-Za-z0-9].*");
    }

    // Counts how many rules pass and returns the strength label
    public static String strength(String pw)
    {
        int count = 0;

        if (hasLength(pw))
        {
            count++;
        }

        if (hasUpperCase(pw))
        {
            count++;
        }

        if (hasDigit(pw))
        {
            count++;
        }

        if (hasSpecialChar(pw))
        {
            count++;
        }

        // 0-1 = Weak, 2-3 = Medium, 4 = Strong
        if (count <= 1)
        {
            return "Weak";
        }
        else if (count <= 3)
        {
            return "Medium";
        }
        else
        {
            return "Strong";
        }
    }

    // Driver code: testing several passwords
    public static void main(String[] args)
    {
        // Passwords to test
        String[] passwords = { "abc", "abcdefgh", "Ab1", "Abcd1234", "Abcd1234!" };

        for (int i = 0; i < passwords.length; i++)
        {
            String pw = passwords[i];

            // Printing which rules passed
            System.out.println("Password     : " + pw);
            System.out.println("Length >= 8  : " + hasLength(pw));
            System.out.println("Uppercase    : " + hasUpperCase(pw));
            System.out.println("Digit        : " + hasDigit(pw));
            System.out.println("Special char : " + hasSpecialChar(pw));

            // Printing the final label
            System.out.println("Strength     : " + strength(pw));
            System.out.println();
        }
    }
}