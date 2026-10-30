public class GenericContainersDemo {

    static class Box<T> {
        private T value;

        Box() {
        }

        Box(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }

        void set(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Box(" + value + ")";
        }
    }

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

        void setFirst(A first) {
            this.first = first;
        }

        void setSecond(B second) {
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair(" + first + ", " + second + ")";
        }
    }

    static <T> T first(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    public static void main(String[] args) {

        System.out.println("--- Box ---");
        Box<String> nameBox = new Box<>("Dhruvi");
        String name = nameBox.get();
        System.out.println("Name: " + name);

        Box<Integer> ageBox = new Box<>(20);
        int nextAge = ageBox.get() + 1;
        System.out.println("Age next year: " + nextAge);

        Box<Double> priceBox = new Box<>();
        priceBox.set(99.5);
        double doublePrice = priceBox.get() * 2;
        System.out.println("Double price: " + doublePrice);

        System.out.println("--- Pair ---");
        Pair<String, Integer> student = new Pair<>("Dhruvi", 87);
        String studentName = student.getFirst();
        int marks = student.getSecond();
        System.out.println(studentName + " scored " + marks);

        Pair<Integer, Boolean> result = new Pair<>(101, true);
        System.out.println(result);

        student.setSecond(92);
        System.out.println(student);

        System.out.println("--- First element ---");
        String[] names = {"Dhruvi", "Amit", "Riya"};
        Integer[] numbers = {10, 20, 30};
        Double[] prices = {9.5, 8.25};
        String[] empty = {};

        String firstName = first(names);
        int firstNumber = first(numbers);
        double firstPrice = first(prices);

        System.out.println("First name: " + firstName);
        System.out.println("First number: " + firstNumber);
        System.out.println("First price: " + firstPrice);
        System.out.println("First of empty array: " + first(empty));
    }
}