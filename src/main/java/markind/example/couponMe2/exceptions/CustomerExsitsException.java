package markind.example.couponMe2.exceptions;

public class CustomerExsitsException extends Exception {
	public CustomerExsitsException() {
		super("Customer with the same email or Id exists");
	}
}
