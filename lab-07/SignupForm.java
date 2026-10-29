public class SignupForm {

    @NotBlank
    @MaxLength(10)
    private String username;

    @NotBlank
    @MaxLength(30)
    private String email;

    @NotBlank
    @MaxLength(12)
    private String password;

    @MaxLength(20)
    private String fullName;

    public SignupForm(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }
}