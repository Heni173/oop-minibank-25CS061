import java.util.*;

class Customer
{
    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    private static long customerCounter = 101;

    private static String generateCustomerId()
    {
        return "CUST" + customerCounter++;
    }

    public Customer(String name, String email, String mobile)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        customerId = generateCustomerId();
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getMobile()
    {
        return mobile;
    }

    public String getCustomerId()
    {
        return customerId;
    }
}

class Account
{
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static int accountCounter = 1;

    private static String generateAccountNumber()
    {
        return String.format("AC%04d", accountCounter++);
    }

    public Account(String ownerName, long balance)
    {
        accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    public Account(String ownerName)
    {
        this(ownerName,0);
    }

    public void deposit(long amount)
    {
        balance = balance + amount;
    }

    public boolean withdraw(long amount)
    {
        if(amount <= balance)
        {
            balance = balance - amount;
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public long getBalance()
    {
        return balance;
    }

    public boolean getActive()
    {
        return active;
    }
}

public class MiniBank
{
    record BankInfo(String name, String branch){}

    enum MenuOption
    {
        OPEN_ACCOUNT,DEPOSITE,WITHDRAW,TRANSFER,EXIT
    }

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank","Main Branch");

        Customer c1 = new Customer("ABC","ABC@gmail.com","798XXXXXXX");

        Account acc[] = new Account[3];

        acc[0] = new Account("ABC",5000);
        acc[1] = new Account("XYZ",3000);
        acc[2] = new Account("PQR");

        while(true)
        {
            System.out.println("--------------------" + bank.name() + "--------------------");
            System.out.println("1. Open Account");
            System.out.println("2. Deposite");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice : ");

            int choice = sc.nextInt();

            switch(choice)
            {
                case 1:

                    System.out.println("Customer ID : " + c1.getCustomerId());
                    System.out.println("Customer Name : " + c1.getName());

                    for(int i=0;i<3;i++)
                    {
                        System.out.println("Account Number : " + acc[i].getAccountNumber());
                        System.out.println("Owner Name : " + acc[i].getOwnerName());
                        System.out.println("Balance : " + acc[i].getBalance());
                    }

                    break;

                case 2:

                    acc[0].deposit(1000);
                    acc[1].deposit(500);
                    acc[2].deposit(2000);

                    System.out.println("Deposit Successful");

                    for(int i=0;i<3;i++)
                    {
                        System.out.println(acc[i].getAccountNumber() + " Balance : " + acc[i].getBalance());
                    }

                    break;

                case 3:

                    if(acc[0].withdraw(2000))
                        System.out.println("Withdrawal Successful");
                    else
                        System.out.println("Insufficient Balance");

                    if(acc[1].withdraw(5000))
                        System.out.println("Insufficient Balance");

                    if(acc[2].withdraw(500))
                        System.out.println("Withdrawal Successful");

                    for(int i=0;i<3;i++)
                    {
                        System.out.println(acc[i].getAccountNumber() + " Balance : " + acc[i].getBalance());
                    }

                    break;

                case 4:

                    System.out.println("Transfer - to be implemented in next lab");
                    break;

                case 5:

                    System.out.println("Successfully exited from your account");
                    sc.close();
                    return;

                default:

                    System.out.println("Invalid choice!! Please try again.");
            }
        }
    }
}