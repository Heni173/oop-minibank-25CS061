import java.util.*;
import java.util.regex.Pattern;

class Customer implements Cloneable
{
    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private Address address;

    private static long customerCounter = 101;

    private static String generateCustomerId()
    {
        return "CUST" + customerCounter++;
    }

    public static class Address
    {
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode)
        {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine()
        {
            return line;
        }

        public String getCity()
        {
            return city;
        }

        public String getPincode()
        {
            return pincode;
        }
    }

    public Customer(String name, String email, String mobile, Address address)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
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

    public Address getAddress()
    {
        return address;
    }

    @Override
    public Customer clone()
    {
        try
        {
            return (Customer) super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
    }
}

abstract class Account
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

    public void deposit(long amount)
    {
        balance = balance + amount;
    }

    public boolean withdraw(long amount)
    {
        if(canWithdraw(amount))
        {
            balance = balance - amount;
            return true;
        }

        return false;
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

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public String toString()
    {
        return "Account Number : " + accountNumber +
               ", Owner Name : " + ownerName +
               ", Balance : " + balance;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(!(o instanceof Account))
        {
            return false;
        }

        Account other = (Account)o;

        return accountNumber.equals(other.accountNumber);
    }
}

class SavingsAccount extends Account
{
    private long minBalance;

    public SavingsAccount(String ownerName, long balance, long minBalance)
    {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate()
    {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount)
    {
        return getBalance() - amount >= minBalance;
    }

    public long getMinBalance()
    {
        return minBalance;
    }
}

class CurrentAccount extends Account
{
    private long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit)
    {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate()
    {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount)
    {
        return getBalance() - amount >= -overdraftLimit;
    }

    public long getOverdraftLimit()
    {
        return overdraftLimit;
    }
}

class FixedDepositAccount extends Account
{
    public FixedDepositAccount(String ownerName, long balance)
    {
        super(ownerName, balance);
    }

    @Override
    public double interestRate()
    {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount)
    {
        return false;
    }
}

class Validator
{
    private static final Pattern MOBILE =
        Pattern.compile("^[6-9][0-9]{9}$");

    private static final Pattern EMAIL =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PAN =
        Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    private static final Pattern IFSC =
        Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    public static boolean isValidMobile(String mobile)
    {
        return MOBILE.matcher(mobile).matches();
    }

    public static boolean isValidEmail(String email)
    {
        return EMAIL.matcher(email).matches();
    }

    public static boolean isValidPan(String pan)
    {
        return PAN.matcher(pan).matches();
    }

    public static boolean isValidIfsc(String ifsc)
    {
        return IFSC.matcher(ifsc).matches();
    }
}

enum TransactionType
{
    DEPOSIT,
    WITHDRAW,
    TRANSFER
}

record Command(TransactionType type, String accountNumber, long amount)
{
}

class CommandParser
{
    public static Command parse(String line)
    {
        String[] parts = line.split(" ");

        TransactionType type =
            TransactionType.valueOf(parts[0]);

        String accountNumber = parts[1];

        long amount = Long.parseLong(parts[2]);

        return new Command(type, accountNumber, amount);
    }
}

class StatementFormatter
{
    public static String buildStatement(Account account)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("----- Account Statement -----\n");
        sb.append("Account Number : ");
        sb.append(account.getAccountNumber());
        sb.append("\n");

        sb.append("Owner Name     : ");
        sb.append(account.getOwnerName());
        sb.append("\n");

        sb.append("Balance        : ");
        sb.append(account.getBalance());
        sb.append("\n");

        sb.append("Interest Rate  : ");
        sb.append(account.interestRate());
        sb.append("%\n");

        sb.append("Active         : ");
        sb.append(account.getActive());

        return sb.toString();
    }
}

public class MiniBank
{
    record BankInfo(String name, String branch)
    {
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        BankInfo bank =
            new BankInfo("MiniBank", "Main Branch");

        Customer.Address address =
            new Customer.Address(
                "123 Main Road",
                "Vadodara",
                "390001"
            );

        Customer c1 =
            new Customer(
                "ABC",
                "ABC@gmail.com",
                "7984561230",
                address
            );

        Account[] acc = new Account[3];

        acc[0] =
            new SavingsAccount("ABC", 5000, 1000);

        acc[1] =
            new CurrentAccount("XYZ", 3000, 2000);

        acc[2] =
            new FixedDepositAccount("PQR", 10000);

        System.out.println("----- Account Details -----");

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(acc[i]);
        }

        System.out.println("\n----- Interest Rate -----");

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(
                acc[i].getAccountNumber() +
                " : " +
                acc[i].interestRate() +
                "%"
            );
        }

        System.out.println("\n----- instanceof -----");

        if(acc[0] instanceof SavingsAccount)
        {
            System.out.println(
                "acc[0] is a SavingsAccount"
            );
        }

        System.out.println("\n----- equals() -----");

        Account testAccount = acc[0];

        if(acc[0].equals(testAccount))
        {
            System.out.println(
                "Both Account objects are equal"
            );
        }
        else
        {
            System.out.println(
                "Both Account objects are not equal"
            );
        }

        System.out.println("\n----- Customer Address -----");

        System.out.println(
            "Line : " +
            c1.getAddress().getLine()
        );

        System.out.println(
            "City : " +
            c1.getAddress().getCity()
        );

        System.out.println(
            "Pincode : " +
            c1.getAddress().getPincode()
        );

        Customer c2 = c1.clone();

        System.out.println("\n----- Clone -----");

        System.out.println(
            "Original : " + c1.getName()
        );

        System.out.println(
            "Clone : " + c2.getName()
        );

        System.out.println("\n----- Validator Testing -----");

        System.out.println(
            "Mobile Correct : " +
            Validator.isValidMobile("7984561230")
        );

        System.out.println(
            "Mobile Wrong   : " +
            Validator.isValidMobile("1234567890")
        );

        System.out.println(
            "Email Correct  : " +
            Validator.isValidEmail("abc@gmail.com")
        );

        System.out.println(
            "Email Wrong    : " +
            Validator.isValidEmail("abc@gmail")
        );

        System.out.println(
            "PAN Correct    : " +
            Validator.isValidPan("ABCDE1234F")
        );

        System.out.println(
            "PAN Wrong      : " +
            Validator.isValidPan("ABC12345")
        );

        System.out.println(
            "IFSC Correct   : " +
            Validator.isValidIfsc("SBIN0001234")
        );

        System.out.println(
            "IFSC Wrong     : " +
            Validator.isValidIfsc("SBI1234")
        );

        System.out.println("\n----- Command Parser -----");

        Command command =
            CommandParser.parse(
                "DEPOSIT AC0001 500"
            );

        System.out.println(
            "Transaction Type : " +
            command.type()
        );

        System.out.println(
            "Account Number   : " +
            command.accountNumber()
        );

        System.out.println(
            "Amount           : " +
            command.amount()
        );

        System.out.println(
            StatementFormatter.buildStatement(acc[0])
        );

        System.out.println("\n----- Withdrawal Testing -----");

        if(acc[0].withdraw(3000))
        {
            System.out.println(
                "Savings Withdrawal Successful"
            );
        }
        else
        {
            System.out.println(
                "Savings Withdrawal Failed"
            );
        }

        if(acc[1].withdraw(4000))
        {
            System.out.println(
                "Current Withdrawal Successful"
            );
        }
        else
        {
            System.out.println(
                "Current Withdrawal Failed"
            );
        }

        if(acc[2].withdraw(1000))
        {
            System.out.println(
                "Fixed Deposit Withdrawal Successful"
            );
        }
        else
        {
            System.out.println(
                "Fixed Deposit Withdrawal Failed"
            );
        }

        while(true)
        {
            System.out.println(
                "\n--------------------" +
                bank.name() +
                "--------------------"
            );

            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice : ");

            int choice = sc.nextInt();

            switch(choice)
            {
                case 1:

                    System.out.println(
                        "\nCustomer ID : " +
                        c1.getCustomerId()
                    );

                    System.out.println(
                        "Customer Name : " +
                        c1.getName()
                    );

                    for(int i = 0; i < acc.length; i++)
                    {
                        System.out.println(acc[i]);
                    }

                    break;

                case 2:

                    acc[0].deposit(1000);
                    acc[1].deposit(500);
                    acc[2].deposit(2000);

                    System.out.println(
                        "Deposit Successful"
                    );

                    for(int i = 0; i < acc.length; i++)
                    {
                        System.out.println(
                            acc[i].getAccountNumber() +
                            " Balance : " +
                            acc[i].getBalance()
                        );
                    }

                    break;

                case 3:

                    System.out.print(
                        "Enter withdrawal amount : "
                    );

                    long amount = sc.nextLong();

                    for(int i = 0; i < acc.length; i++)
                    {
                        if(acc[i].withdraw(amount))
                        {
                            System.out.println(
                                acc[i].getAccountNumber() +
                                " Withdrawal Successful"
                            );
                        }
                        else
                        {
                            System.out.println(
                                acc[i].getAccountNumber() +
                                " Withdrawal Failed"
                            );
                        }
                    }

                    break;

                case 4:

                    System.out.println(
                        "Transfer - to be implemented in next lab"
                    );

                    break;

                case 5:

                    System.out.println(
                        "Successfully exited from your account"
                    );

                    sc.close();
                    return;

                default:

                    System.out.println(
                        "Invalid choice!! Please try again."
                    );
            }
        }
    }
}