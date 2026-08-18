abstract class Employee
{
    public String name;
    public int id;

    Employee(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    abstract public double monthlySalary();
}

class FullTime extends Employee
{
    double salary;

    FullTime(String name, int id, double salary)
    {
        super(name, id);
        this.salary = salary;
    }

    @Override
    public double monthlySalary()
    {
        return salary;
    }
}

class PartTime extends Employee
{
    private int hours;
    private float rate;

    PartTime(String name, int id, int hours, float rate)
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

class Intern extends Employee
{
    double stipend;

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

public class EmployeeDemo
{
    public static void main(String[] a)
    {
        Employee[] employees = {
            new FullTime("ABC", 101, 100000),
            new PartTime("XYZ", 102, 12, 1.2f),
            new Intern("PQR", 103, 15000)
        };

        for (Employee e : employees)
        {
            System.out.println("Name: "+e.name);
            System.out.println("ID: "+e.id);

            if(e instanceof Intern)
            {
                System.out.println("Intern don't have salary they have stipend");
            }
            else
            {
                System.out.println("Salary: "+e.monthlySalary());
            }
            System.out.println("====================================================");
        }
    }
}