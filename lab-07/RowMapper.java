import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RowMapper {

    public static <T> T map(Class<T> type, String[] header, String[] row) throws Exception {
        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            index.put(header[i].trim().toLowerCase(), i);
        }

        T object = type.getDeclaredConstructor().newInstance();

        for (Field field : type.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }

            Integer position = index.get(column.name().toLowerCase());
            String value = null;
            if (position != null && position < row.length) {
                value = row[position].trim();
            }

            if (value == null || value.isEmpty()) {
                if (column.required()) {
                    throw new IllegalArgumentException("Missing required column: " + column.name());
                }
                continue;
            }

            field.setAccessible(true);
            field.set(object, convert(field.getType(), value));
        }
        return object;
    }

    private static Object convert(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Unsupported field type: " + type.getSimpleName());
    }
}