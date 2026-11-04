// Program: Functional interface with lambdas, array of senders and a marker interface

// Functional interface (only one abstract method)
@FunctionalInterface
interface Notifier
{
    void send(String message);
}

// Marker interface: it has no methods, it only "marks" a sender as urgent
interface Urgent
{
}

// Driver class
public class NotificationDemo
{
    public static void main(String[] args)
    {
        // Email sender as a normal lambda
        Notifier email = msg -> System.out.println("Email: " + msg);

        // SMS sender as a lambda that is also marked Urgent
        // (Notifier & Urgent) makes the lambda implement both interfaces
        Notifier sms = (Notifier & Urgent) msg -> System.out.println("SMS: " + msg);

        // Holding all senders in one array
        Notifier[] senders = {email, sms};

        String message = "Server will restart at 10 PM";

        System.out.println("Broadcasting message:");

        // Broadcast one message through all senders
        for (Notifier sender : senders)
        {
            sender.send(message);

            // Urgent senders send the message one more time
            if (sender instanceof Urgent)
            {
                System.out.println("  (urgent sender, sending again)");
                sender.send(message);
            }
        }
    }
}