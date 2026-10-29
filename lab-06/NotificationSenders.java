
@FunctionalInterface
interface Notifier
{
    void send(String message);
}

interface Urgent
{
}

class EmailSender implements Notifier
{
    public void send(String message)
    {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements Notifier
{
    public void send(String message)
    {
        System.out.println("SMS: " + message);
    }
}

class UrgentEmailSender implements Notifier, Urgent
{
    public void send(String message)
    {
        System.out.println("Urgent Email: " + message);
    }
}

public class NotificationSenders
{
    public static void main(String[] args)
    {
        Notifier email = (message) ->
        {
            System.out.println("Email: " + message);
        };

        Notifier sms = (message) ->
        {
            System.out.println("SMS: " + message);
        };

        Notifier urgentEmail = new UrgentEmailSender();

        Notifier[] senders = {email, sms, urgentEmail};

        String message = "Assignment submission is tomorrow.";

        System.out.println("Broadcasting Notifications:");

        for (Notifier sender : senders)
        {
            sender.send(message);

            if (sender instanceof Urgent)
            {
                sender.send(message);
            }
        }
    }
}
