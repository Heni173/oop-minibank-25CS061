package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.MaxLength;
import model.annotation.Positive;

// Utility class for validating objects using custom annotations.
public class AnnotationValidator
{
    // Validates the fields of an object and returns validation errors.
    public static String[] validate(Object obj)
    {
        List<String> errors = new ArrayList<>();

        Class<?> cls = obj.getClass();

        // Traverses the class hierarchy to check all declared fields.
        while(cls != null)
        {
            Field[] fields = cls.getDeclaredFields();

            // Iterates through each field of the current class.
            for(Field field : fields)
            {
                field.setAccessible(true);

                try
                {
                    Object value = field.get(obj);

                    // Checks whether the field has the Positive annotation.
                    if(field.isAnnotationPresent(Positive.class))
                    {
                        Positive p = field.getAnnotation(Positive.class);

                        if(value instanceof Number n && n.doubleValue() <= 0)
                        {
                            errors.add(field.getName() + " " + p.message());
                        }
                    }

                    // Checks whether the field has the MaxLength annotation.
                    if(field.isAnnotationPresent(MaxLength.class))
                    {
                        MaxLength m = field.getAnnotation(MaxLength.class);

                        if(value instanceof String s && s.length() > m.value())
                        {
                            errors.add(field.getName() + " length must be <= " + m.value());
                        }
                    }
                }
                catch(IllegalAccessException e)
                {
                    errors.add(field.getName() + " could not be read");
                }
            }

            cls = cls.getSuperclass();
        }

        // Returns all validation errors as a string array.
        return errors.toArray(new String[0]);
    }
}