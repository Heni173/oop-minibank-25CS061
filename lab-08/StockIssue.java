// Program: Warehouse stock issue using custom checked exceptions

// Checked exception thrown when stock is not enough (carries the shortfall)
class OutOfStockException extends Exception
{
    private int shortfall;   // how many items are short

    OutOfStockException(int shortfall)
    {
        super("Out of stock");
        this.shortfall = shortfall;
    }

    public int getShortfall()
    {
        return shortfall;
    }
}

// Checked exception thrown when quantity is zero or negative
class InvalidQuantityException extends Exception
{
    InvalidQuantityException(String message)
    {
        super(message);
    }
}

class Warehouse
{
    // Items and their stock (same index means same item)
    private String[] items = {"Laptop", "Mouse", "Keyboard"};
    private int[] stock = {5, 10, 3};

    // Issues qty items, or throws an exception if it is not possible
    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException
    {
        // Quantity must be greater than 0
        if (qty <= 0)
        {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        // Finding the item in the list (-1 means not found)
        int index = -1;

        for (int i = 0; i < items.length; i++)
        {
            if (items[i].equalsIgnoreCase(item))
            {
                index = i;
                break;
            }
        }

        // If the item is not found, available stock is 0
        int available = 0;

        if (index != -1)
        {
            available = stock[index];
        }

        // Not enough stock: throw exception with the shortfall
        if (qty > available)
        {
            int shortfall = qty - available;
            throw new OutOfStockException(shortfall);
        }

        // Reducing the stock and printing the result
        stock[index] = stock[index] - qty;

        System.out.println("Issued " + qty + " " + item);
    }

    // Prints the remaining stock of every item
    public void showStock()
    {
        for (int i = 0; i < items.length; i++)
        {
            System.out.println(items[i] + " = " + stock[i]);
        }
    }
}

public class StockIssue
{
    public static void main(String[] args)
    {
        Warehouse warehouse = new Warehouse();

        // List of requests: item name and quantity
        String[][] requests =
        {
            {"Laptop", "2"},
            {"Mouse", "15"},
            {"Keyboard", "2"},
            {"Laptop", "0"},
            {"Monitor", "3"},
            {"Mouse", "4"}
        };

        // Processing every request, a failure does not stop the loop
        for (int i = 0; i < requests.length; i++)
        {
            String item = requests[i][0];
            int qty = Integer.parseInt(requests[i][1]);

            try
            {
                warehouse.issue(item, qty);
            }
            catch (OutOfStockException e)
            {
                System.out.println("Failed: " + item + " - " + e.getMessage()
                        + ", shortfall = " + e.getShortfall());
            }
            catch (InvalidQuantityException e)
            {
                System.out.println("Failed: " + item + " - " + e.getMessage());
            }
        }

        System.out.println("All requests processed.");
        System.out.println();
        System.out.println("Remaining stock:");
        warehouse.showStock();
    }
}