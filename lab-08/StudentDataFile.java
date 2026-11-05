// Program: try-with-resources with AutoCloseable (resource always closes)

// Resource: a student data file (AutoCloseable lets us use it in try-with-resources)
class StudentResource implements AutoCloseable
{
    private boolean fail;   // true means errors will be thrown

    StudentResource(boolean fail)
    {
        this.fail = fail;
        System.out.println("Student data file opened");
    }

    void readData()
    {
        System.out.println("Reading student data...");

        // Error inside the try block
        if (fail)
        {
            throw new RuntimeException("Error while reading student data");
        }

        System.out.println("Data read successfully");
    }

    // close() is called automatically at the end of the try block
    public void close()
    {
        System.out.println("Student data file closed");

        // Second error, thrown while closing
        if (fail)
        {
            throw new RuntimeException("Error while closing student data file");
        }
    }
}

public class StudentDataFile
{
    public static void main(String[] args)
    {
        // Test 1: no error, file still closes
        System.out.println("Test 1: no error");

        try (StudentResource file = new StudentResource(false))
        {
            file.readData();
        }
        catch (RuntimeException e)
        {
            System.out.println("Reported error: " + e.getMessage());
        }

        System.out.println();

        // Test 2: error inside the block AND error while closing
        System.out.println("Test 2: error inside the block and also while closing");

        try (StudentResource file = new StudentResource(true))
        {
            file.readData();
        }
        catch (RuntimeException e)
        {
            // The original error is the one reported
            System.out.println("Reported error: " + e.getMessage());

            // The close() error is not lost, it is stored as suppressed
            Throwable[] hidden = e.getSuppressed();
            System.out.println("Suppressed errors: " + hidden.length);

            for (Throwable t : hidden)
            {
                System.out.println("  Suppressed: " + t.getMessage());
            }
        }

        System.out.println("Program continues after both tests.");
    }
}