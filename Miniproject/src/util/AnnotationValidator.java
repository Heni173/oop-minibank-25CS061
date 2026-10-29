package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator
{
    public static String[] validate(Object obj)
    {
        List<String> errors = new ArrayList<>();

        Class<?> cls = obj.getClass();

        while(cls != null)
        {
            Field[] fields = cls.getDeclaredFields();

            for(Field field : fields)
            {
                field.setAccessible(true);

                try
                {
                    Object value = field.get(obj);

                    if(field.isAnnotationPresent(Positive.class))
                    {
                        Positive p = field.getAnnotation(Positive.class);

                        if(value instanceof Number n && n.doubleValue() <= 0)
                        {
                            errors.add(field.getName() + " " + p.message());
                        }
                    }

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

        return errors.toArray(new String[0]);
    }
}