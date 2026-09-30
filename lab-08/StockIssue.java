class OutOfStockException extends Exception
{
    int shortfall;

    OutOfStockException(int shortfall)
    {
        this.shortfall = shortfall;
    }

    public int getShortfall()
    {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception
{
    InvalidQuantityException(String message)
    {
        super(message);
    }
}

class Warehouse
{
    private String[] items = {"Laptop", "Mouse", "Keyboard"};
    private int[] stock = {5, 10, 3};

    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException
    {
        if (qty <= 0)
        {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        int index = -1;

        for (int i = 0; i < items.length; i++)
        {
            if (items[i].equalsIgnoreCase(item))
            {
                index = i;
                break;
            }
        }

        int available = 0;

        if (index != -1)
        {
            available = stock[index];
        }

        if (qty > available)
        {
            int shortfall = qty - available;
            throw new OutOfStockException(shortfall);
        }

        stock[index] = stock[index] - qty;

        System.out.println("Issued " + qty + " " + item);
    }
}

public class StockIssue
{
    public static void main(String[] args)
    {
        Warehouse warehouse = new Warehouse();

        String[][] requests =
        {
            {"Laptop", "2"},
            {"Mouse", "15"},
            {"Keyboard", "2"},
            {"Laptop", "0"},
            {"Monitor", "3"},
            {"Mouse", "4"}
        };

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
                System.out.println("Failed: " + item +" - Shortfall = " + e.getShortfall());
            }
            catch (InvalidQuantityException e)
            {
                System.out.println("Failed: " + item +" - " + e.getMessage());
            }
        }

        System.out.println("All requests processed.");
    }
}