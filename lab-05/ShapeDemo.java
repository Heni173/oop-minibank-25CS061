// Program: Abstract class Shape with polymorphism

// Abstract base class with abstract method area()
abstract class Shape
{
    abstract public double area();
}

// Circle class
class Circle extends Shape
{
    private float radius;

    Circle(float radius)
    {
        this.radius = radius;
    }

    // Area of circle = PI * r * r
    public double area()
    {
        return Math.PI * radius * radius;
    }
}

// Rectangle class
class Rectangle extends Shape
{
    private float height, width;

    Rectangle(float height, float width)
    {
        this.height = height;
        this.width = width;
    }

    // Area of rectangle = height * width
    public double area()
    {
        return height * width;
    }
}

// Triangle class
class Triangle extends Shape
{
    private float base;
    private float height;

    Triangle(float base, float height)
    {
        this.base = base;
        this.height = height;
    }

    // Area of triangle = 0.5 * base * height
    public double area()
    {
        return 0.5 * base * height;
    }
}

// Driver class
public class ShapeDemo
{
    public static void main(String[] a)
    {
        // Mix of different shapes in one Shape array
        Shape[] shapes = {
            new Circle(15),
            new Rectangle(10, 20),
            new Triangle(5, 10)
        };

        double total = 0;        // running total of all areas
        double largest = -1;     // largest area found so far
        String label = "";       // shape name having the largest area

        // One loop handles every shape through polymorphism
        for (Shape s : shapes)
        {
            double area = s.area();     // correct area() is called at runtime
            total += area;

            String name = s.getClass().getSimpleName();

            // Printing area of this shape and the running total
            System.out.printf("%s area = %.2f | Running total = %.2f%n", name, area, total);

            // Tracking the largest area
            if (largest < area)
            {
                largest = area;
                label = name;
            }
        }

        System.out.println();
        System.out.printf("Total area = %.2f%n", total);
        System.out.printf("Largest area = %.2f (%s)%n", largest, label);
    }
}