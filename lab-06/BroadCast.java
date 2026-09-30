interface Urgent{}
interface Notifier
{
    void send(String msg);
}

class SMS implements Notifier,Urgent
{
    public void send(String msg)
    {
        System.out.println("SMS"+msg);
    }
}
class Email implements Notifier
{
    public void send(String msg)
    {
        System.out.println("Email"+msg);
    }
}

public class BroadCast
{
    public static void main(String []a)
    {
        Notifier[] notifiers={
            new SMS(),
            new Email()
        };

        for(Notifier n : notifiers)
        {
            n.send(" Server Down");

            if (n instanceof Urgent)
            {
                n.send(" Urgent Server Down");
            }
        }
    }
}


