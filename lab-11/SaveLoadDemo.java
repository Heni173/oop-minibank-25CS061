// Program: Saving and loading objects using Serializable and ObjectOutputStream

import java.io.*;

public class SaveLoadDemo
{
    // Serializable means objects of this class can be saved to a file
    static class Student implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int id;
        String name;
        int marks;

        // transient means this field will NOT be saved in the file
        transient String password;

        Student(int id, String name, int marks, String password)
        {
            this.id = id;
            this.name = name;
            this.marks = marks;
            this.password = password;
        }

        public String toString()
        {
            return "Student{id=" + id + ", name=" + name + ", marks=" + marks
                    + ", password=" + password + "}";
        }
    }

    public static void main(String[] args) throws Exception
    {
        // Array of objects to save
        Student[] students = {
            new Student(1, "Heni", 85, "heni@123"),
            new Student(2, "Riya", 90, "riya@456"),
            new Student(3, "Krisha", 78, "krisha@789")
        };

        System.out.println("Before saving:");
        for (Student s : students)
        {
            System.out.println(s);
        }

        // Saving the whole array into the file students.dat
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("students.dat")))
        {
            out.writeObject(students);
        }

        System.out.println();
        System.out.println("Saved " + students.length + " students to students.dat");

        // Reading the array back from the file into fresh objects
        Student[] loaded;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("students.dat")))
        {
            loaded = (Student[]) in.readObject();
        }

        System.out.println();
        System.out.println("After loading:");
        for (Student s : loaded)
        {
            System.out.println(s);
        }

        // Checking that id, name and marks are the same as before
        boolean same = true;

        for (int i = 0; i < students.length; i++)
        {
            if (students[i].id != loaded[i].id
                    || !students[i].name.equals(loaded[i].name)
                    || students[i].marks != loaded[i].marks)
            {
                same = false;
            }
        }

        System.out.println();
        System.out.println("Values survived (id, name, marks): " + same);
        System.out.println("Loaded objects are new objects: " + (loaded[0] != students[0]));
        System.out.println("Password (transient) was saved: " + (loaded[0].password != null));
    }
}