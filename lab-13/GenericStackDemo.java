// Program: Generic Stack using Array

public class GenericStackDemo {

    // Generic Stack class
    static class Stack<T> {

        private T[] items;
        private int top;

        // Constructor
        @SuppressWarnings("unchecked")
        Stack(int size) {
            items = (T[]) new Object[size];
            top = -1;
        }

        // Add element to stack
        void push(T value) {

            if (top == items.length - 1) {
                System.out.println("Stack is full!");
                return;
            }

            top++;
            items[top] = value;
            System.out.println(value + " added");
        }

        // Remove element from stack
        T pop() {

            if (top == -1) {
                System.out.println("Stack is empty!");
                return null;
            }

            T value = items[top];
            items[top] = null;
            top--;

            return value;
        }

        // Check whether stack is empty
        boolean isEmpty() {
            return top == -1;
        }
    }

    public static void main(String[] args) {

        // Stack of Integer
        System.out.println("--- Integer Stack ---");

        Stack<Integer> numberStack = new Stack<>(5);

        numberStack.push(10);
        numberStack.push(20);
        numberStack.push(30);

        System.out.println("Removed: " + numberStack.pop());
        System.out.println("Removed: " + numberStack.pop());

        // Stack of String
        System.out.println("\n--- String Stack ---");

        Stack<String> nameStack = new Stack<>(5);

        nameStack.push("Heni");
        nameStack.push("Krisha");
        nameStack.push("Riya");

        System.out.println("Removed: " + nameStack.pop());
        System.out.println("Removed: " + nameStack.pop());

        // Checking empty case
        System.out.println("\n--- Empty Stack Check ---");

        while (!nameStack.isEmpty()) {
            System.out.println("Removed: " + nameStack.pop());
        }

        System.out.println("Removed: " + nameStack.pop());
    }
}