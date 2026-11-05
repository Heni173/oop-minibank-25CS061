// Program: Filling an object from a header row and a data row using reflection

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

// Annotation that gives the column name of a field
// RUNTIME is needed so that reflection can read it while the program runs
@Retention(RetentionPolicy.RUNTIME)
@interface Column
{
    String name();
}

// Class whose fields are linked to column names
class Student
{
    @Column(name = "student_id")
    private int id;

    @Column(name = "student_name")
    private String name;

    @Column(name = "marks")
    private int marks;

    // This column is not present in the header, so it will stay null
    @Column(name = "email")
    private String email;

    public String toString()
    {
        return "Student{id=" + id + ", name=" + name + ", marks=" + marks
                + ", email=" + email + "}";
    }
}

public class ColumnDemo
{
    // Fills the fields of obj using the header row and the data row
    public static void fill(Object obj, String[] header, String[] data) throws Exception
    {
        // Getting all fields of the class
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            // Only fields having @Column are filled
            if (!field.isAnnotationPresent(Column.class))
            {
                continue;
            }

            // Reading the column name written in the annotation
            Column c = field.getAnnotation(Column.class);
            String columnName = c.name();

            // Finding this column name in the header row
            int index = -1;

            for (int i = 0; i < header.length; i++)
            {
                if (header[i].equals(columnName))
                {
                    index = i;
                    break;
                }
            }

            // Missing column: print a warning and keep the default value
            if (index == -1 || index >= data.length)
            {
                System.out.println("Warning: column '" + columnName
                        + "' not found, field '" + field.getName()
                        + "' is left as default");
                continue;
            }

            // Fields are private, so we allow access
            field.setAccessible(true);

            String text = data[index];

            // Converting the text according to the type of the field
            if (field.getType() == int.class)
            {
                field.setInt(obj, Integer.parseInt(text));
            }
            else
            {
                field.set(obj, text);
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        // Header row and data row (the "email" column is missing)
        String[] header = {"student_id", "student_name", "marks"};
        String[] data = {"101", "Heni", "85"};

        Student s = new Student();

        fill(s, header, data);

        System.out.println(s);
    }
}