package markind.example.couponMe2.exceptions;

public class InvalidRequestException extends Exception {
	public InvalidRequestException(String msg) {
		super(msg);
	}
	
	public InvalidRequestException() {
		super("invalid request");
	}
}
