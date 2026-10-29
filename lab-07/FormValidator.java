import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FormValidator {

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                errors.add(field.getName() + ": cannot read value");
                continue;
            }

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value == null || value.toString().trim().isEmpty()) {
                    errors.add(field.getName() + ": must not be blank");
                }
            }

            if (field.isAnnotationPresent(MaxLength.class) && value != null) {
                int max = field.getAnnotation(MaxLength.class).value();
                int length = value.toString().length();
                if (length > max) {
                    errors.add(field.getName() + ": length is " + length
                            + " but maximum allowed is " + max);
                }
            }
        }
        return errors;
    }
}