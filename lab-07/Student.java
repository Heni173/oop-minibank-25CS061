public class Student {

    @Column(name = "roll_no")
    private int rollNo;

    @Column(name = "name")
    private String name;

    @Column(name = "marks")
    private double marks;

    @Column(name = "city", required = false)
    private String city;

    @Override
    public String toString() {
        return "Student{rollNo=" + rollNo + ", name=" + name
                + ", marks=" + marks + ", city=" + city + "}";
    }
}