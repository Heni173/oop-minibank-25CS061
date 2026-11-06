package storage;

import java.io.*;
import java.util.ArrayList;
import model.Account;

// Storage class: saves and loads the list of accounts from a file
public class AccountStorage
{
    // Saves all accounts into the file
    public static void save(ArrayList<Account> accounts, String fileName) throws IOException
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            out.writeObject(accounts);
        }
    }

    // Loads all accounts from the file (empty list if the file does not exist)
    @SuppressWarnings("unchecked")
    public static ArrayList<Account> load(String fileName) throws IOException, ClassNotFoundException
    {
        File file = new File(fileName);

        if (!file.exists())
        {
            return new ArrayList<Account>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (ArrayList<Account>) in.readObject();
        }
    }
}