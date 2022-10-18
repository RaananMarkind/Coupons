package markind.example.couponMe2.job;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.db.repositories.CouponRepository;

@Service
public class ExpiredCouponJob implements Runnable {
	
	boolean work = false;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Override
	public void run() {
		
		while(!work) {
			LocalDate date = LocalDate.now();
			List<Coupon> coupons = couponRepository.findAll();
			
			for (Coupon coupon : coupons) {
				//check if coupon is outdated
				if(coupon.getEndDate().isBefore(date)) {
					//remove it from the customer
					couponRepository.removeByCustomersCouponId(coupon.getId());
					//remove it from the coupons
					couponRepository.deleteById(coupon.getId());
				}
			}
			
			try {
				//run every 24 hours
				Thread.sleep(1000*60*60*24);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			
		}

	}
	
	public void worker () {
		if(work) 
			work = false;
		else
			work = true;
	}
}
