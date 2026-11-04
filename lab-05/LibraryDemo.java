// Program: Library late fee calculator using abstract class and polymorphism
// Rules: Book = Rs.2/day after 2 grace days, DVD = Rs.5/day after 1 grace day,
//        Magazine = Rs.1/day after 3 grace days

// Abstract base class for all media types
abstract class Media
{
    private String title;
    private int lateDays;      // number of days the item was returned late

    Media(String title, int lateDays)
    {
        this.title = title;
        this.lateDays = lateDays;
    }

    public String getTitle()
    {
        return title;
    }

    public int getLateDays()
    {
        return lateDays;
    }

    // Each media type calculates its late fee differently
    abstract double lateFee();
}

// Book: 2 grace days, Rs.2 per day after that
class Book extends Media
{
    Book(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 2;

        if (getLateDays() > graceDays)
        {
            return (getLateDays() - graceDays) * 2;
        }

        return 0;
    }
}

// DVD: 1 grace day, Rs.5 per day after that
class DVD extends Media
{
    DVD(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 1;

        if (getLateDays() > graceDays)
        {
            return (getLateDays() - graceDays) * 5;
        }

        return 0;
    }
}

// Magazine: 3 grace days, Rs.1 per day after that
class Magazine extends Media
{
    Magazine(String title, int lateDays)
    {
        super(title, lateDays);
    }

    double lateFee()
    {
        int graceDays = 3;

        if (getLateDays() > graceDays)
        {
            return (getLateDays() - graceDays) * 1;
        }

        return 0;
    }
}

// Driver class
public class LibraryDemo
{
    public static void main(String[] args)
    {
        // Returned batch with different media types
        Media[] returnedBatch =
        {
            new Book("Java Programming", 5),
            new DVD("Avengers Movie", 3),
            new Magazine("Tech Magazine", 4)
        };

        double totalFee = 0;   // total late fee of the whole batch

        for (Media m : returnedBatch)
        {
            double fee = m.lateFee();   // correct fee rule is called at runtime

            System.out.printf("%-9s %-17s Late days = %d | Late Fee: Rs. %.2f%n",
                    m.getClass().getSimpleName(), m.getTitle(), m.getLateDays(), fee);

            totalFee = totalFee + fee;
        }

        System.out.println();
        System.out.printf("Total Late Fee: Rs. %.2f%n", totalFee);
    }
}