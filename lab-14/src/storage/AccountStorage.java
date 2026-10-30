package storage;

import model.Account;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AccountStorage {

    public static void save(List<Account> accounts, Path file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(accounts.size());
            for (Account account : accounts) {
                out.writeInt(account.getId());
                out.writeUTF(account.getHolderName());
                out.writeDouble(account.getBalance());
            }
        }
    }

    public static List<Account> load(Path file) throws IOException {
        List<Account> accounts = new ArrayList<>();
        if (!Files.exists(file)) {
            return accounts;
        }
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                int id = in.readInt();
                String name = in.readUTF();
                double balance = in.readDouble();
                accounts.add(new Account(id, name, balance));
            }
        }
        return accounts;
    }
}