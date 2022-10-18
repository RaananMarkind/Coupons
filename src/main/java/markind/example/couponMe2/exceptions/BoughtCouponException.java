package markind.example.couponMe2.exceptions;

public class BoughtCouponException extends Exception {
	public BoughtCouponException () {
		super("Customer already bought this coupon");
	}
}
