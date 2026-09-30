interface Switchable
{
    void turnOn();

    void turnOff();

    boolean isOn();

    default void toggle()
    {
        if(isOn())
        {
            turnOff();
        }
        else
        {
            turnOn();
        }
    }
}
interface SwitchOnRule
{
    boolean maySwitchOn(Switchable device,int hour);
}


class Light implements Switchable
{
    boolean state = false;

    public void turnOn()
    {
        state = true;
        System.out.println("Light is on");
    }

    public void turnOff()
    {
        state = false;
        System.out.println("Light is off");
    }

    public boolean isOn()
    {
        return state;
    }

}

class Fan implements Switchable
{
    boolean state = false;

    public void turnOn()
    {
        state = true;
        System.out.println("Fan is on");
    }

    public void turnOff()
    {
        state = false;
        System.out.println("Fan is off");
    }

    public boolean isOn()
    {
        return state;
    }

}

public class RemoteControl
{
    public static void main(String []a)
    {
        Switchable[] devices = {
            new Light(),
            new Fan()
        };

        for(Switchable d : devices)
        {
            d.toggle();
        }

       // DayTimeRule rule = new DayTimeRule();
        SwitchOnRule rule = new SwitchOnRule()
        {
            public boolean maySwitchOn(Switchable device,int hour)
            {
                return (hour>=6 && hour<=22);
            }

        };

        if(rule.maySwitchOn(devices[0],21))
        {
            devices[0].turnOn();
        }
        else
        {
            System.out.println("Cannot On the light at this time");
        }
    }
}

