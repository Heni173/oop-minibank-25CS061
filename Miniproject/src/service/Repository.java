package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.annotation.Id;

public class Repository<T>
{
    private final Map<String, T> items = new HashMap<>();

    private String extractId(T entity)
    {
        Class<?> cls = entity.getClass();

        while(cls != null)
        {
            Field[] fields = cls.getDeclaredFields();

            for(Field field : fields)
            {
                if(field.isAnnotationPresent(Id.class))
                {
                    try
                    {
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

        throw new IllegalArgumentException(
            "No @Id field found in " + entity.getClass().getSimpleName()
        );
    }

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

    public T findById(String id)
    {
        return items.get(id);
    }

    public ArrayList<T> findAll()
    {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id)
    {
        return items.remove(id) != null;
    }

    public int size()
    {
        return items.size();
    }

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