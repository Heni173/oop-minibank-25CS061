import java.util.Scanner;

public class PasswordChecker
{
    public static Boolean hashLength(String pw)
    {
        return pw.length() >= 8;
    }

    public static Boolean hashUpperCase(String pw)
    {
        return pw.matches(".*[A-Z].*");
    }

    public static Boolean hashDigit(String pw)
    {
        return pw.matches(".*[0-9].*");
    }

    public static Boolean hashSpecialChar(String pw)
    {
        return pw.matches(".*[@#%&].*");
    }

    public static String getStrength(String pw)
    {
        int count = 0;

        if(hashLength(pw))
            count++;

        if(hashUpperCase(pw))
            count++;

        if(hashDigit(pw))
            count++;

        if(hashSpecialChar(pw))
            count++;

        String strength = switch(count)
        {
            case 0, 1 -> "Weak";
            case 2, 3 -> "Medium";
            case 4 -> "Strong";
            default -> "Invalid";
        };

        return strength;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Password: ");
        String pw = sc.nextLine();

        System.out.println("Password Strength: " + getStrength(pw));

        sc.close();
    }
}