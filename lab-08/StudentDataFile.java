class StudentResource implements AutoCloseable
{
    StudentResource()
    {
        System.out.println("Student data file opened");
    }

    void readData()
    {
        System.out.println("Reading student data...");
        throw new RuntimeException("Error while reading student data");
    }

    public void close()
    {
        System.out.println("Student data file closed");
    }
}

public class StudentDataFile
{
    public static void main(String[] args)
    {
        try (StudentResource file = new StudentResource())
        {
            file.readData();
        }
        catch (RuntimeException e)
        {
            System.out.println("Reported error: " + e.getMessage());
        }
    }
}