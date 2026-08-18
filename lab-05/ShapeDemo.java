abstract class Shape
{
    abstract public double area();
}

class Circle extends Shape
{
    float radius;

    Circle(float radius)
    {
        this.radius = radius;
    }

    public double area()
    {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape
{
    float height, width;

    Rectangle(float height, float width)
    {
        this.height = height;
        this.width = width;
    }

    public double area()
    {
        return height * width;
    }
}

class Triangle extends Shape
{
    float base;
    float height;

    Triangle(float base, float height)
    {
        this.base = base;
        this.height = height;
    }

    public double area()
    {
        return 0.5 * base * height;
    }
}

class ShapeDemo
{
    public static void main(String[] a)
    {
        Shape[] shapes = {
            new Circle(15),
            new Rectangle(10, 20),
            new Triangle(5, 10)
        };

        double total=0;
        double largest=-1;
        String label="";

        for(Shape s : shapes)
        {
            System.out.println("The area is " + s.area());
            total+=s.area();
            if(largest< s.area())
            {
                largest=s.area();
                label=s.getClass().getSimpleName();
            }
        }
        System.out.println("The total area is "+total);
        System.out.println("The largest area is "+ largest+"of shape "+label);

    }
}