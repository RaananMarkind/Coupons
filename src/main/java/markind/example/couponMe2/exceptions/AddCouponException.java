package markind.example.couponMe2.exceptions;

public class AddCouponException extends Exception {
	public AddCouponException () {
		super("Can't add a coupon with the same title.");
	}
}
