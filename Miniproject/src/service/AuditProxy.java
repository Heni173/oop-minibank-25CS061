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

public class AuditProxy implements InvocationHandler
{
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Object target;
    private final Path auditFile;

    private AuditProxy(Object target, Path auditFile)
    {
        this.target = target;
        this.auditFile = auditFile;
    }

    public static <T> T wrap(T target, Class<T> type, Path auditFile)
    {
        Object proxy =
            Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] { type },
                new AuditProxy(target, auditFile)
            );

        return type.cast(proxy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Method real =
            target.getClass().getMethod(method.getName(), method.getParameterTypes());

        Audit audit = real.getAnnotation(Audit.class);

        try
        {
            Object result = method.invoke(target, args);

            if(audit != null)
            {
                record(real, audit, args, "OK");
            }

            return result;
        }
        catch(InvocationTargetException e)
        {
            Throwable cause = e.getCause();

            if(audit != null)
            {
                record(
                    real, audit, args,
                    "FAILED " + cause.getClass().getSimpleName() + " : " + cause.getMessage()
                );
            }

            throw cause;
        }
    }

    private void record(Method method, Audit audit, Object[] args, String outcome)
    {
        String arguments = Arrays.toString(args);

        if(arguments.length() > 100)
        {
            arguments = arguments.substring(0, 100) + "...]";
        }

        String line =
            LocalDateTime.now().format(FORMAT) +
            " | " + method.getName() +
            " | " + audit.value() +
            " | args=" + arguments +
            " | " + outcome;

        TransactionLog.append(auditFile, line);
    }

    public static List<String> readTrail(Path auditFile)
    {
        if(!Files.exists(auditFile))
        {
            return new ArrayList<>();
        }

        try
        {
            return Files.readAllLines(auditFile);
        }
        catch(IOException e)
        {
            System.out.println("Could not read audit trail : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}