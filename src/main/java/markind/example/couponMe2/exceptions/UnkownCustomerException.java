package markind.example.couponMe2.exceptions;

public class UnkownCustomerException extends Exception {
	public UnkownCustomerException() {
		super("Failed to fatch a customer...");
	}
}
