package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import model.Account;

public class StatePersister
{
    public static void save(Account[] accounts, Path file)
    {
        try
        {
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

    public static Account[] load(Path file)
    {
        try(ObjectInputStream in =
                new ObjectInputStream(Files.newInputStream(file)))
        {
            return (Account[]) in.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println("Could not load accounts : " + e.getMessage());
            return new Account[0];
        }
    }
}