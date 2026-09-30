class Printer
{
    synchronized void printDoc(String docName,int totalPage)
    {
        System.out.println("Printing by"+Thread.currentThread().getName());
        
        for(int i=0;i<totalPage;i++)
        {
            System.out.println(Thread.currentThread().getName()+ " Printing "+ docName + " Page No. " + i + " of " + totalPage);

            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
}

class Pc implements Runnable
{
    Printer p;
    String docName;
    int totalPage;

    Pc(Printer p,String docName,int totalPage)
    {
        this.p=p;
        this.docName=docName;
        this.totalPage=totalPage;
    }

    public void run()
    {
        p.printDoc(docName,totalPage);

    }
}

public class PrintDemo
{
    public static void main(String[] args)
    {
        Printer p= new Printer();
        Pc pc1 = new Pc(p , "Incometax",10);
        Pc pc2 = new Pc(p , "Java",20);

        Thread t1 = new Thread(pc1,"PC1");
        Thread t2 = new Thread(pc2,"PC2");

        t1.start();
        t2.start();
        
        
    }
}