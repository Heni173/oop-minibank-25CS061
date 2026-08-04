import java.util.*;

record Vehicle (String number , String type){}

class TollBooth
{
  public static void main(String a[])
    {
        int totalToll=0;
        int bike=0;
        int car=0;
        int truck=0;

        Scanner sc = new Scanner(System.in);

        while(true)
        {
            String type=sc.next();

            if(type.equalsIgnoreCase("done"))
            break;

            String number = sc.next();
            Vehicle v = new Vehicle(number,type);
            int toll=0;

            switch(v.type())
            {
              case "Bike" : toll=20;
              bike++;
              break;

              case "Car" : toll=50;
              car++;
              break;

              case "Truck" : toll=150;
              truck++;
              break;

            default : toll=0;
            }
            
            System.out.println(v.type() + " passed with the toll=" + toll);
            totalToll+=toll;
        } 
          System.out.println("Total bike passed : " + bike);
          System.out.println("Total car passed : " + bike);
          System.out.println("Total truck passed : " + bike);
          System.out.println("Total toll collected =  " + totalToll);
        }
}