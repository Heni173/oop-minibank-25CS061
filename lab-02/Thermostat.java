// Program: Thermostat class using static members and constructor chaining

class Thermostat
{
    // Constants for minimum and maximum temperature
    private static final int MIN = 16;
    private static final int MAX = 30;

    // Instance variables
    private String location;
    private int temperature;

    // Static variable to count total thermostats created
    private static int activeCount = 0;

    // Constructor with location and starting temperature
    public Thermostat(String location, int startTemp)
    {
        this.location = location;

        // If start temperature is out of range, set default 22
        if (startTemp > MAX || startTemp < MIN)
        {
            temperature = 22;
        }
        else
        {
            temperature = startTemp;
        }

        // Increase count whenever a new object is created
        activeCount++;
    }

    // Constructor with only location (default temperature 22)
    public Thermostat(String location)
    {
        this(location, 22);   // calling the other constructor
    }

    // Increase temperature by 1 if it is less than MAX
    public void raise()
    {
        if (temperature < MAX)
        {
            temperature++;
        }
        else
        {
            System.out.println("Already at maximum (30)");
        }
    }

    // Decrease temperature by 1 if it is greater than MIN
    public void lower()
    {
        if (temperature > MIN)
        {
            temperature--;
        }
        else
        {
            System.out.println("Already at minimum (16)");
        }
    }

    // Getter for temperature (no setter as per the question)
    public int getTemperature()
    {
        return temperature;
    }

    // Static method to get total number of thermostats
    public static int getActiveCount()
    {
        return activeCount;
    }

    public static void main(String args[])
    {
        // Creating two thermostat objects
        Thermostat t1 = new Thermostat("Nadiad", 22);
        Thermostat t2 = new Thermostat("Anand");

        // Calling raise() 10 times and printing temperature each time
        System.out.println("---- Raising Temperature ----");
        for (int i = 1; i <= 10; i++)
        {
            t1.raise();
            System.out.println("Raise " + i + " : Temperature = " + t1.getTemperature());
        }

        // Calling lower() 20 times and printing temperature each time
        System.out.println("---- Lowering Temperature ----");
        for (int i = 1; i <= 20; i++)
        {
            t1.lower();
            System.out.println("Lower " + i + " : Temperature = " + t1.getTemperature());
        }

        // Printing total number of thermostats created
        System.out.println("Active Count = " + Thermostat.getActiveCount());
    }
}