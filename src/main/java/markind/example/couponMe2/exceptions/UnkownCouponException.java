package markind.example.couponMe2.exceptions;

public class UnkownCouponException extends Exception {
	public UnkownCouponException () {
		super("coupon was not found or data didn't match...");
	}
}