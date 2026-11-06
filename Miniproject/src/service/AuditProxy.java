package service;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.annotation.Audit;

// AuditProxy handles method calls and records audit information using a dynamic proxy.
public class AuditProxy implements InvocationHandler
{
    // Defines the date and time format used in the audit trail.
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Stores the actual object whose methods will be called.
    private final Object target;

    // Stores the path of the audit log file.
    private final Path auditFile;

    // Private constructor to initialize the target object and audit file path.
    private AuditProxy(Object target, Path auditFile)
    {
        this.target = target;
        this.auditFile = auditFile;
    }

    // Creates and returns a dynamic proxy for the given target object.
    public static <T> T wrap(T target, Class<T> type, Path auditFile)
    {
        Object proxy =
            Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] { type },
                new AuditProxy(target, auditFile)
            );

        // Converts the created proxy into the required type.
        return type.cast(proxy);
    }

    // Intercepts method calls made through the dynamic proxy.
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Gets the actual method from the target class.
        Method real =
            target.getClass().getMethod(method.getName(), method.getParameterTypes());

        // Checks whether the actual method has the Audit annotation.
        Audit audit = real.getAnnotation(Audit.class);

        try
        {
            // Invokes the requested method on the actual target object.
            Object result = method.invoke(target, args);

            // Records the method call if the Audit annotation is present.
            if(audit != null)
            {
                record(real, audit, args, "OK");
            }

            return result;
        }
        catch(InvocationTargetException e)
        {
            // Gets the original exception thrown by the target method.
            Throwable cause = e.getCause();

            // Records the failed method call if the Audit annotation is present.
            if(audit != null)
            {
                record(
                    real, audit, args,
                    "FAILED " + cause.getClass().getSimpleName() + " : " + cause.getMessage()
                );
            }

            // Throws the original exception to the caller.
            throw cause;
        }
    }

    // Creates an audit log entry and appends it to the audit file.
    private void record(Method method, Audit audit, Object[] args, String outcome)
    {
        // Converts the method arguments into a string.
        String arguments = Arrays.toString(args);

        // Limits the argument string length to 100 characters.
        if(arguments.length() > 100)
        {
            arguments = arguments.substring(0, 100) + "...]";
        }

        // Creates the complete audit log entry.
        String line =
            LocalDateTime.now().format(FORMAT) +
            " | " + method.getName() +
            " | " + audit.value() +
            " | args=" + arguments +
            " | " + outcome;

        // Appends the audit entry to the transaction log file.
        TransactionLog.append(auditFile, line);
    }

    // Reads and returns all entries from the audit trail file.
    public static List<String> readTrail(Path auditFile)
    {
        // Returns an empty list if the audit file does not exist.
        if(!Files.exists(auditFile))
        {
            return new ArrayList<>();
        }

        try
        {
            // Reads all lines from the audit file.
            return Files.readAllLines(auditFile);
        }
        catch(IOException e)
        {
            // Displays an error message if the audit file cannot be read.
            System.out.println("Could not read audit trail : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}