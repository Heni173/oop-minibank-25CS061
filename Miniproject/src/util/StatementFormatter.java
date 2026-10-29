package util;

import model.Account;

public class StatementFormatter
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

        sb.append("Yearly Interest: ");
        sb.append(account.yearlyInterest());
        sb.append("\n");

        sb.append("Active         : ");
        sb.append(account.getActive());

        return sb.toString();
    }
}