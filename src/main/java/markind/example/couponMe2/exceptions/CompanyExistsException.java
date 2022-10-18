package markind.example.couponMe2.exceptions;

public class CompanyExistsException extends Exception {
	public CompanyExistsException () {
		super("Email or companys' name is taken");
	}
}
