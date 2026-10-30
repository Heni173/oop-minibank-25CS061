import java.util.Arrays;

public class GenericStackDemo {

    static class MyStack<T> {
        private T[] items;
        private int size;

        @SuppressWarnings("unchecked")
        MyStack() {
            items = (T[]) new Object[2];
        }

        void push(T item) {
            if (size == items.length) {
                items = Arrays.copyOf(items, items.length * 2);
            }
            items[size++] = item;
        }

        T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            T item = items[--size];
            items[size] = null;
            return item;
        }

        T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return items[size - 1];
        }

        boolean isEmpty() {
            return size == 0;
        }

        int size() {
            return size;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(items[i]);
            }
            return sb.append("]").toString();
        }
    }

    public static void main(String[] args) {

        System.out.println("--- Integer stack ---");
        MyStack<Integer> numbers = new MyStack<>();
        numbers.push(10);
        numbers.push(20);
        numbers.push(30);
        numbers.push(40);
        numbers.push(50);
        System.out.println("Stack: " + numbers);
        System.out.println("Top: " + numbers.peek());
        int popped = numbers.pop();
        System.out.println("Popped: " + popped);
        System.out.println("Size: " + numbers.size());
        int sum = numbers.pop() + numbers.pop();
        System.out.println("Sum of next two pops: " + sum);
        System.out.println("Size: " + numbers.size());

        System.out.println("--- String stack ---");
        MyStack<String> words = new MyStack<>();
        words.push("apple");
        words.push("banana");
        words.push("cherry");
        System.out.println("Stack: " + words);
        while (!words.isEmpty()) {
            System.out.println("Popped: " + words.pop());
        }

        System.out.println("--- Empty case ---");
        System.out.println("Is empty: " + words.isEmpty());
        try {
            words.pop();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            words.peek();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}