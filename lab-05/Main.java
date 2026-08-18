abstract class Media
{
    String title;
    int lateDays;

    Media(String title, int lateDays)
    {
        this.title = title;
        this.lateDays = lateDays;
    }

    abstract double lateFee();
}

class Book extends Media
{
    Book(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 2;

        if(lateDays > graceDays)
        {
            return (lateDays - graceDays) * 2;
        }

        return 0;
    }
}

class DVD extends Media
{
    DVD(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 1;

        if(lateDays > graceDays)
        {
            return (lateDays - graceDays) * 5;
        }

        return 0;
    }
}

class Magazine extends Media
{
    Magazine(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 3;

        if(lateDays > graceDays)
        {
            return (lateDays - graceDays) * 1;
        }

        return 0;
    }
}

public class Main
{
    public static void main(String[] args)
    {
        Media[] returnedBatch =
        {
            new Book("Java Programming", 5),
            new DVD("Avengers Movie", 3),
            new Magazine("Tech Magazine", 4)
        };

        double totalFee = 0;

        for(Media m : returnedBatch)
        {
            double fee = m.lateFee();

            System.out.println(m.title + " - Late Fee: ₹" + fee);

            totalFee = totalFee + fee;
        }

        System.out.println("\nTotal Late Fee: ₹" + totalFee);
    }
}
