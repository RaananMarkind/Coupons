package markind.example.couponMe2.exceptions;

public class UnkownCompanyException extends Exception {
	public UnkownCompanyException () {
		super("Unknown company. Name, Id or email don't match");
	}
}
