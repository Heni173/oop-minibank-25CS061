public class MyTests {

    @Run
    public void addTest() {
        if (2 + 3 != 5) {
            throw new RuntimeException("addition is wrong");
        }
    }

    @Run
    public void subtractTest() {
        if (9 - 4 != 5) {
            throw new RuntimeException("subtraction is wrong");
        }
    }

    @Run
    public void divideByZeroTest() {
        int result = 10 / 0;
        System.out.println(result);
    }

    public void skippedTest() {
        System.out.println("skippedTest was called");
    }

    public void helper() {
        System.out.println("helper was called");
    }
}