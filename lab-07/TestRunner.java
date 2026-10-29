import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class TestRunner {

    public static void runAll(Class<?> testClass) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();

        Method[] methods = testClass.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        int ran = 0;
        int passed = 0;
        int failed = 0;

        for (Method method : methods) {
            if (!method.isAnnotationPresent(Run.class)) {
                continue;
            }
            if (method.getParameterCount() != 0) {
                continue;
            }

            method.setAccessible(true);
            ran++;

            try {
                method.invoke(instance);
                passed++;
                System.out.println("PASS: " + method.getName());
            } catch (InvocationTargetException e) {
                failed++;
                System.out.println("FAIL: " + method.getName() + " (" + e.getCause() + ")");
            }
        }

        System.out.println("Total methods run: " + ran);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }
}