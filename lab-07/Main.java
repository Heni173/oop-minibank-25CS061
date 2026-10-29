import java.util.List;

public class Main {
    public static void main(String[] args) {

        SignupForm good = new SignupForm("heni", "heni@mail.com", "pass1234", "Heni Patel");
        printResult("Good form", FormValidator.validate(good));

        SignupForm bad = new SignupForm("   ", "thisisaverylongemailaddress@example.com",
                "abc123456789012", "Heni Patel");
        printResult("Bad form", FormValidator.validate(bad));
    }

    static void printResult(String title, List<String> errors) {
        System.out.println("--- " + title + " ---");
        if (errors.isEmpty()) {
            System.out.println("There is no error, form is correct");
        } else {
            for (String e : errors) {
                System.out.println(e);
            }
        }
    }
}