// Program: Form validator using custom annotations and reflection

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;

// Annotation: field must not be blank
// RUNTIME is needed so that reflection can read it while the program runs
@Retention(RetentionPolicy.RUNTIME)
@interface NotBlank
{
}

// Annotation: field must not be longer than the given value
@Retention(RetentionPolicy.RUNTIME)
@interface MaxLength
{
    int value();
}

// Form class with annotated fields
class SignupForm
{
    @NotBlank
    @MaxLength(10)
    private String username;

    @NotBlank
    @MaxLength(25)
    private String email;

    @NotBlank
    @MaxLength(12)
    private String password;

    // No annotation here, so it will not be checked
    private String city;

    public SignupForm(String username, String email, String password, String city)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
    }
}

// Checker class that uses reflection
class FormValidator
{
    // Returns the list of errors (empty list means the form is valid)
    public static ArrayList<String> validate(Object obj) throws Exception
    {
        ArrayList<String> errors = new ArrayList<String>();

        // Getting all fields of the class
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            // Fields are private, so we allow access
            field.setAccessible(true);

            // Reading the value of the field
            String value = (String) field.get(obj);

            // Checking @NotBlank
            if (field.isAnnotationPresent(NotBlank.class))
            {
                if (value == null || value.trim().isEmpty())
                {
                    errors.add(field.getName() + " must not be blank");
                }
            }

            // Checking @MaxLength
            if (field.isAnnotationPresent(MaxLength.class))
            {
                MaxLength m = field.getAnnotation(MaxLength.class);
                int max = m.value();

                if (value != null && value.length() > max)
                {
                    errors.add(field.getName() + " must be at most " + max
                            + " characters (found " + value.length() + ")");
                }
            }
        }

        return errors;
    }
}

public class FormValidatorDemo
{
    public static void main(String[] args) throws Exception
    {
        // Form 1: all values are correct
        SignupForm f1 = new SignupForm("heni", "heni@gmail.com", "secret123", "Surat");

        // Form 2: blank username, long email and long password
        SignupForm f2 = new SignupForm("   ", "this.is.a.very.long.email@example.com",
                "averyveryverylongpassword", "Surat");

        // Checking form 1
        System.out.println("Form 1:");
        ArrayList<String> errors1 = FormValidator.validate(f1);

        if (errors1.isEmpty())
        {
            System.out.println("  No errors. Form is valid.");
        }
        else
        {
            for (String e : errors1)
            {
                System.out.println("  " + e);
            }
        }

        // Checking form 2
        System.out.println();
        System.out.println("Form 2:");
        ArrayList<String> errors2 = FormValidator.validate(f2);

        if (errors2.isEmpty())
        {
            System.out.println("  No errors. Form is valid.");
        }
        else
        {
            for (String e : errors2)
            {
                System.out.println("  " + e);
            }
        }
    }
}