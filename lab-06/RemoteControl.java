// Program: Interface with default method, functional interface, anonymous class and lambda

interface Switchable
{
    void on();
    void off();
    boolean isOn();     // added: tells the current state of the device

    // Default method: if device is ON turn it OFF, otherwise turn it ON
    default void toggle()
    {
        if (isOn())
        {
            off();
        }
        else
        {
            on();
        }
    }
}

class Fan implements Switchable
{
    private boolean running = false;     // state of the fan

    public void on()
    {
        running = true;
        System.out.println("Fan is ON");
    }

    public void off()
    {
        running = false;
        System.out.println("Fan is OFF");
    }

    public boolean isOn()
    {
        return running;
    }
}

class Light implements Switchable
{
    private boolean glowing = false;     // state of the light

    public void on()
    {
        glowing = true;
        System.out.println("Light is ON");
    }

    public void off()
    {
        glowing = false;
        System.out.println("Light is OFF");
    }

    public boolean isOn()
    {
        return glowing;
    }
}

// Functional interface: only one abstract method
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

        // Toggling twice to show ON first and then OFF
        System.out.println("Toggling Devices (first time):");

        for (Switchable device : devices)
        {
            device.toggle();
        }

        System.out.println("\nToggling Devices (second time):");

        for (Switchable device : devices)
        {
            device.toggle();
        }

        // Permission rule as an anonymous class: all devices allowed between 6 and 22
        SwitchPermission permission1 = new SwitchPermission()
        {
            public boolean canSwitchOn(Switchable device, int hour)
            {
                return hour >= 6 && hour <= 22;
            }
        };

        // Permission rule as a lambda: Fan allowed between 6 and 22, Light any time
        SwitchPermission permission2 = (device, hour) ->
        {
            if (device instanceof Fan)
            {
                return hour >= 6 && hour <= 22;
            }
            return true;
        };

        int[] hours = {10, 23};      // hours to test

        System.out.println("\nUsing Anonymous Class:");

        for (int hour : hours)
        {
            for (Switchable device : devices)
            {
                System.out.println("Hour " + hour + " -> "
                        + device.getClass().getSimpleName() + ": "
                        + (permission1.canSwitchOn(device, hour)
                        ? "can be switched ON"
                        : "cannot be switched ON"));
            }
        }

        System.out.println("\nUsing Lambda Expression:");

        for (int hour : hours)
        {
            for (Switchable device : devices)
            {
                System.out.println("Hour " + hour + " -> "
                        + device.getClass().getSimpleName() + ": "
                        + (permission2.canSwitchOn(device, hour)
                        ? "can be switched ON"
                        : "cannot be switched ON"));
            }
        }
    }
}