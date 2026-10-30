import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveLoadDemo {

    static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        String name;
        int marks;
        transient String password;

        Student(String name, int marks, String password) {
            this.name = name;
            this.marks = marks;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Student{name=" + name + ", marks=" + marks + ", password=" + password + "}";
        }
    }

    public static void main(String[] args) {
        String fileName = "students.dat";

        Student[] original = {
            new Student("Amit", 85, "amit123"),
            new Student("Riya", 92, "riya456"),
            new Student("Kiran", 78, "kiran789")
        };

        System.out.println("Before saving:");
        for (Student s : original) {
            System.out.println(s);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(original);
            System.out.println("\nSaved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Student[] loaded;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            loaded = (Student[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("\nAfter loading:");
        for (Student s : loaded) {
            System.out.println(s);
        }

        System.out.println();
        for (int i = 0; i < original.length; i++) {
            boolean sameName = original[i].name.equals(loaded[i].name);
            boolean sameMarks = original[i].marks == loaded[i].marks;
            boolean passwordSaved = loaded[i].password != null;
            System.out.println(original[i].name + " -> name same: " + sameName
                    + ", marks same: " + sameMarks
                    + ", password saved: " + passwordSaved);
        }

        System.out.println("\nSame object? " + (original[0] == loaded[0]));
    }
}
