package util;

import model.Account;

// Utility class for formatting account statements.
public class StatementFormatter
{
    // Builds and returns a formatted statement for the given account.
    public static String buildStatement(Account account)
    {
        StringBuilder sb = new StringBuilder();

        // Adds the account statement heading.
        sb.append("----- Account Statement -----\n");
        sb.append("Account Number : ");
        sb.append(account.getAccountNumber());
        sb.append("\n");

        // Adds the account owner's name.
        sb.append("Owner Name     : ");
        sb.append(account.getOwnerName());
        sb.append("\n");

        // Adds the current account balance.
        sb.append("Balance        : ");
        sb.append(account.getBalance());
        sb.append("\n");

        // Adds the applicable interest rate.
        sb.append("Interest Rate  : ");
        sb.append(account.interestRate());
        sb.append("%\n");

        // Adds the calculated yearly interest.
        sb.append("Yearly Interest: ");
        sb.append(account.yearlyInterest());
        sb.append("\n");

        // Adds the account's active status.
        sb.append("Active         : ");
        sb.append(account.getActive());

        return sb.toString();
    }
}