// Program: Bounded Generics to calculate average

public class BoundedStatsDemo {

    // Generic method with Number as upper bound
    static <N extends Number> double average(N[] values) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(
                "Array cannot be empty or null"
            );
        }

        double sum = 0;

        for (int i = 0; i < values.length; i++) {
            sum = sum + values[i].doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {

        // Integer array
        Integer[] intValues = {10, 20, 30, 40};

        // Double array
        Double[] doubleValues = {10.5, 20.5, 30.5};

        // Long array
        Long[] longValues = {100L, 200L, 300L};

        System.out.println("--- Average of Different Number Types ---");

        System.out.println("Integer Average: " + average(intValues));

        System.out.println("Double Average: " + average(doubleValues));

        System.out.println("Long Average: " + average(longValues));
    }
}