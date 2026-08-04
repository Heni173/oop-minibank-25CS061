import java.util.*;

class VendingMachine     
{
   enum Coin{ONE,TWO,FIVE,TEN}
  
   public static void main(String a[])
     {
        System.out.println(Coin.TWO);
        final int PRICE=15;
        int total=0;
        Coin c=null;

        Scanner sc = new Scanner(System.in);

        while(total<=PRICE)
         {
           System.out.println("Enter the coin");
           String input = sc.nextLine();

           try
             {
                c=Coin.valueOf(input.toUpperCase());
             }
            catch(Exception e)
             {
                System.out.println("Not a valid coin!!! Please try again......");
                continue;
             }
             
             int value=0;

             switch(c)
             {
                case ONE: 
                total=total+1;
                break;

                case TWO:
                total=total+2;
                break;

                case FIVE:
                total=total+5;
                break;

                case TEN:
                total=total+10;
                break;
             };
               
                System.out.println("Total paid :"+total); 
         }

 
     }

}
