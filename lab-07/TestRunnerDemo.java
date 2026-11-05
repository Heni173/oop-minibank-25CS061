// Program: Mini test runner using annotation and reflection

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

// Annotation to mark the methods that should be run
// RUNTIME is needed so that reflection can read it while the program runs
@Retention(RetentionPolicy.RUNTIME)
@interface Run
{
}

// Class with some methods marked with @Run
class MyTests
{
    @Run
    public void testAdd()
    {
        System.out.println("  2 + 3 = " + (2 + 3));
    }

    @Run
    public void testSubtract()
    {
        System.out.println("  5 - 2 = " + (5 - 2));
    }

    @Run
    public void testMultiply()
    {
        System.out.println("  2 * 3 = " + (2 * 3));
    }

    // These two methods have no @Run, so they will not be run
    public void helperMethod()
    {
        System.out.println("  helperMethod is running (should not happen)");
    }

    public void notATest()
    {
        System.out.println("  notATest is running (should not happen)");
    }
}

public class TestRunnerDemo
{
    public static void main(String[] args) throws Exception
    {
        MyTests tests = new MyTests();

        // Getting all methods of the MyTests class
        Method[] methods = MyTests.class.getDeclaredMethods();

        int count = 0;

        System.out.println("Running tests...");

        for (Method m : methods)
        {
            // Checking if the method has @Run
            if (m.isAnnotationPresent(Run.class))
            {
                System.out.println("Running: " + m.getName());
                m.invoke(tests);     // calling the method
                count++;
            }
            else
            {
                System.out.println("Skipped (no @Run): " + m.getName());
            }
        }

        System.out.println();
        System.out.println("Total methods run: " + count);
    }
}