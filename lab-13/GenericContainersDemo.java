// Program: Generic Containers using Box, Pair and Generic Method

public class GenericContainersDemo {

    // Generic Box class
    static class Box<T> {
        private T value;

        Box(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }

        void set(T value) {
            this.value = value;
        }
    }

    // Generic Pair class
    static class Pair<A, B> {
        private A first;
        private B second;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        A getFirst() {
            return first;
        }

        B getSecond() {
            return second;
        }
    }

    // Generic method to return first element of an array
    static <T> T first(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        // Using Box with String
        System.out.println("--- Box ---");

        Box<String> nameBox = new Box<>("Heni");
        System.out.println("Name: " + nameBox.get());

        // Changing the value using set()
        nameBox.set("Krisha");
        System.out.println("Updated Name: " + nameBox.get());

        // Using Box with Integer
        Box<Integer> ageBox = new Box<>(19);
        System.out.println("Age: " + ageBox.get());

        // Using Pair with different data types
        System.out.println("\n--- Pair ---");

        Pair<String, Integer> student = new Pair<>("Heni", 90);

        System.out.println("Student Name: " + student.getFirst());
        System.out.println("Student Marks: " + student.getSecond());

        // Another Pair with different types
        Pair<Integer, String> result = new Pair<>(101, "Pass");

        System.out.println("Roll Number: " + result.getFirst());
        System.out.println("Result: " + result.getSecond());

        // Using generic method with different arrays
        System.out.println("\n--- First Element ---");

        String[] names = {"Heni", "Krisha", "Riya"};
        Integer[] numbers = {10, 20, 30};
        Double[] prices = {99.5, 50.5};

        System.out.println("First Name: " + first(names));
        System.out.println("First Number: " + first(numbers));
        System.out.println("First Price: " + first(prices));
    }
}