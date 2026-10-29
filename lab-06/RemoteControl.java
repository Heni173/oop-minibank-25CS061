
interface Switchable
{
    void on();
    void off();

    default void toggle()
    {
        off();
        on();
    }
}

class Fan implements Switchable
{
    public void on()
    {
        System.out.println("Fan is ON");
    }

    public void off()
    {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable
{
    public void on()
    {
        System.out.println("Light is ON");
    }

    public void off()
    {
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchPermission
{
    boolean canSwitchOn(Switchable device, int hour);
}

public class RemoteControl
{
    public static void main(String[] args)
    {
        Switchable[] devices = {new Fan(), new Light()};

        System.out.println("Toggling Devices:");

        for (Switchable device : devices)
        {
            device.toggle();
        }

        SwitchPermission permission1 = new SwitchPermission()
        {
            public boolean canSwitchOn(Switchable device, int hour)
            {
                return hour >= 6 && hour <= 22;
            }
        };

        SwitchPermission permission2 = (device, hour) ->
        {
            return hour >= 6 && hour <= 22;
        };

        int hour = 10;

        System.out.println("\nUsing Anonymous Class:");

        for (Switchable device : devices)
        {
            System.out.println(permission1.canSwitchOn(device, hour)
                    ? "Device can be switched ON"
                    : "Device cannot be switched ON");
        }

        System.out.println("\nUsing Lambda Expression:");

        for (Switchable device : devices)
        {
            System.out.println(permission2.canSwitchOn(device, hour)
                    ? "Device can be switched ON"
                    : "Device cannot be switched ON");
        }
    }
}
