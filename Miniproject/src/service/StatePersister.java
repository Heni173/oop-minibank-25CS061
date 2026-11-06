package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import model.Account;

public class StatePersister
{
    // Saves the whole accounts array to a file using serialization
    // (Account must implement Serializable)
    public static void save(Account[] accounts, Path file)
    {
        try
        {
            // Create the folder first if it does not exist
            if(file.getParent() != null)
            {
                Files.createDirectories(file.getParent());
            }

            try(ObjectOutputStream out =
                    new ObjectOutputStream(Files.newOutputStream(file)))
            {
                out.writeObject(accounts);
            }
        }
        catch(IOException e)
        {
            System.out.println("Could not save accounts : " + e.getMessage());
        }
    }

    // Reads the accounts back from the file. These are new copies, not the original objects.
    public static Account[] load(Path file)
    {
        try(ObjectInputStream in =
                new ObjectInputStream(Files.newInputStream(file)))
        {
            // readObject() returns Object, so it must be cast back to Account[]
            return (Account[]) in.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println("Could not load accounts : " + e.getMessage());

            // Empty array instead of null, so the caller does not get a NullPointerException
            return new Account[0];
        }
    }
}