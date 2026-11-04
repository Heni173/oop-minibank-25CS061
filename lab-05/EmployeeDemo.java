// Program: Abstract class Employee with polymorphism and instanceof

// Abstract base class holding the common name and id
abstract class Employee
{
    private String name;
    private int id;

    Employee(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    // Each type of employee calculates salary in its own way
    abstract public double monthlySalary();
}

// Full time employee with fixed salary
class FullTime extends Employee
{
    private double salary;

    FullTime(String name, int id, double salary)
    {
        super(name, id);     // calling super for shared name and id
        this.salary = salary;
    }

    @Override
    public double monthlySalary()
    {
        return salary;
    }
}

// Part time employee paid by hours x rate
class PartTime extends Employee
{
    private int hours;
    private double rate;

    PartTime(String name, int id, int hours, double rate)
    {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    public double monthlySalary()
    {
        return hours * rate;
    }
}

// Intern gets a stipend
class Intern extends Employee
{
    private double stipend;

    Intern(String name, int id, double stipend)
    {
        super(name, id);
        this.stipend = stipend;
    }

    @Override
    public double monthlySalary()
    {
        return stipend;
    }
}

// Driver class
public class EmployeeDemo
{
    public static void main(String[] a)
    {
        // Mixed array of different employee types
        Employee[] employees = {
            new FullTime("ABC", 101, 100000),
            new PartTime("XYZ", 102, 12, 1.2),
            new Intern("PQR", 103, 15000)
        };

        double total = 0;   // total of all monthly salaries

        for (Employee e : employees)
        {
            double salary = e.monthlySalary();   // correct version called at runtime
            total += salary;

            System.out.println("Name: " + e.getName());
            System.out.println("ID: " + e.getId());
            System.out.printf("Salary: %.2f%n", salary);

            // Note only for interns
            if (e instanceof Intern)
            {
                System.out.println("Note: Intern gets a stipend, not a salary");
            }

            System.out.println("====================================");
        }

        System.out.printf("Total salary = %.2f%n", total);
    }
}