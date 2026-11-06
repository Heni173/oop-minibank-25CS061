package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.annotation.Id;

// Generic repository : stores any type T that has a field marked with @Id
public class Repository<T>
{
    // Key = value of the @Id field, value = the object
    private final Map<String, T> items = new HashMap<>();

    // Finds the @Id field using reflection and returns its value as a String
    private String extractId(T entity)
    {
        Class<?> cls = entity.getClass();

        // Also checks parent classes, because @Id may be declared in a superclass
        while(cls != null)
        {
            Field[] fields = cls.getDeclaredFields();

            for(Field field : fields)
            {
                if(field.isAnnotationPresent(Id.class))
                {
                    try
                    {
                        // Needed to read a private field
                        field.setAccessible(true);

                        Object value = field.get(entity);

                        if(value == null)
                        {
                            throw new IllegalArgumentException(
                                "@Id field " + field.getName() + " is null"
                            );
                        }

                        return String.valueOf(value);
                    }
                    catch(IllegalAccessException e)
                    {
                        throw new IllegalStateException(
                            "Cannot read @Id field : " + field.getName(), e
                        );
                    }
                }
            }

            cls = cls.getSuperclass();
        }

        // No @Id field in the whole class hierarchy
        throw new IllegalArgumentException(
            "No @Id field found in " + entity.getClass().getSimpleName()
        );
    }

    // Same id replaces the old object
    public T save(T entity)
    {
        if(entity == null)
        {
            throw new IllegalArgumentException("Entity must not be null");
        }

        String id = extractId(entity);

        items.put(id, entity);

        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... values)
    {
        for(T value : values)
        {
            save(value);
        }
    }

    // Returns null if the id is not found
    public T findById(String id)
    {
        return items.get(id);
    }

    // Returns a copy, so changing the list does not affect the repository
    public ArrayList<T> findAll()
    {
        return new ArrayList<>(items.values());
    }

    // True only if something was actually removed
    public boolean delete(String id)
    {
        return items.remove(id) != null;
    }

    public int size()
    {
        return items.size();
    }

    // N can be any Number type (Integer, Long, Double, etc.)
    public static <N extends Number> double average(N[] values)
    {
        if(values.length == 0)
        {
            return 0;
        }

        double sum = 0;

        for(N value : values)
        {
            sum = sum + value.doubleValue();
        }

        return sum / values.length;
    }
}