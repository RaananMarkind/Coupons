package markind.example.couponMe2.exceptions;

public class LoginException extends Exception {
	public LoginException() {
		super("Failed to login, try again please");
	}
}
