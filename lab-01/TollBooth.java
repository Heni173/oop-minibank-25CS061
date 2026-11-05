import java.util.*;

record Vehicle(String number, String type) {}

class TollBooth
{
    public static void main(String[] args)
    {
        int totalToll = 0;
        int bike = 0;
        int car = 0;
        int truck = 0;

        Scanner sc = new Scanner(System.in);

        // Taking vehicle details until user enters done
        while(true)
        {
            System.out.print("Enter vehicle number or done:");
            String number = sc.next();

            if(number.equalsIgnoreCase("done"))
                break;

            System.out.print("Enter vehicle type:");
            String type = sc.next().toLowerCase();

            // Creating Vehicle record
            Vehicle v = new Vehicle(number, type);

            // Calculating toll using switch expression
            int toll = switch(v.type())
            {
                case "bike" -> {
                    bike++;
                    yield 20;
                }
                case "car" -> {
                    car++;
                    yield 50;
                }
                case "truck" -> {
                    truck++;
                    yield 150;
                }
                default -> {
                    System.out.println("Invalid vehicle type!");
                    yield 0;
                }
            };

            if(toll > 0)
            {
                totalToll = totalToll + toll;
                System.out.println(v.type() + " passed with toll = " + toll);
            }
        }

        // Printing total toll and vehicle counts
        System.out.println("Total bike passed: " + bike);
        System.out.println("Total car passed: " + car);
        System.out.println("Total truck passed: " + truck);
        System.out.println("Total toll: " + totalToll);

        // Finding the vehicle type with the highest count
        if(bike == 0 && car == 0 && truck == 0)
        {
            System.out.println("Most frequent: None");
        }
        else if(bike >= car && bike >= truck)
        {
            System.out.println("Most frequent: bike");
        }
        else if(car >= bike && car >= truck)
        {
            System.out.println("Most frequent: car");
        }
        else
        {
            System.out.println("Most frequent: truck");
        }

        sc.close();
    }
}
