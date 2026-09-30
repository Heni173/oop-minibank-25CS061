class Counter
{
    int count=0;

    public synchronized void increment()
    {
        count++;
    }
}

public class CounterRace
{
    public static void main(String a[])
    {
        Counter c1=new Counter();
        Thread t1 = new Thread(()->{for (int i=0;i<50000;i++)c1.increment();});

        Counter c2=new Counter();
        Thread t2 = new Thread(()->{for (int i=0;i<50000;i++)c2.increment();});


        t1.start();
        t2.start();

        try 
        {
            t1.join();
            t2.join();
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }

        System.out.println("count:"+c1.count);

    }
}
