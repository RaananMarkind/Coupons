package markind.example.couponMe2.facades;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import markind.example.couponMe2.beans.Category;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.beans.Customer;
import markind.example.couponMe2.db.repositories.CompanyRepository;
import markind.example.couponMe2.db.repositories.CouponRepository;
import markind.example.couponMe2.db.repositories.CustomerRepository;
import markind.example.couponMe2.exceptions.BoughtCouponException;
import markind.example.couponMe2.exceptions.LogisticsCouponException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;

@Service
@Scope("prototype")
public class CustomerFacade extends ManagerFacade {
	private int customerId;
	
	public CustomerFacade(CompanyRepository companyRepository, CouponRepository couponRepository,
			CustomerRepository customerRepository) {
		super(companyRepository, couponRepository, customerRepository);
	}

	@Override
	//login
	public int login(String email, String password) throws SQLException {
		//making sure the details are correct, if so returns 1
		//if not - returns -1
		customerId = -99;
		if(customerRepository.isCustomerExistsByEmail(email, password) != null) {
			customerId = customerRepository.isCustomerExistsByEmail(email, password).getId();
			return 1;
		}
		return -1;
	}
	
	//Customer purchase a new coupon
	public Coupon purchesCoupon(Coupon coupon) throws BoughtCouponException, LogisticsCouponException {
		LocalDate today = LocalDate.now();
		//can't purchase the same coupon:
		if(couponRepository.getAllCouponsByCustomersId(customerId).size() !=0) {
			throw new BoughtCouponException();
		}
		//check if no coupons left or expired coupon:
		else if (coupon.getAmount() <= 0 || coupon.getEndDate().isAfter(today)) {
			throw new LogisticsCouponException();
		}
		
		else {
			//purchasing a coupon and removing it the amount by one
			couponRepository.insertCouponsToCustomer(customerId, coupon.getId());
			coupon.setAmount(coupon.getAmount()-1);
			return coupon;	
		}
	}
	
	//getting customer's coupons
	public ArrayList<Coupon> getCustomersCoupons(){
		ArrayList<Coupon> returnedCoupons = new ArrayList<>();
		ArrayList<Integer> customersCoupons = couponRepository.getAllCouponsByCustomersId(customerId);
		List<Coupon> coupons = couponRepository.findAll();
		//joined table + coupons table:
		//getting customer's current purchase and getting the coupons from
		//coupon's table
		for (int i = 0; i < customersCoupons.size(); i++) {
				for (int j = 0; j < coupons.size(); j++) {
					if(coupons.get(j).getId() == customersCoupons.get(i)) {
						returnedCoupons.add(coupons.get(j));
					}
				}
		}
		return returnedCoupons;
	}
	
	//getting customer's coupons by category
	public ArrayList<Coupon> getCustomersCouponsByCategory(Category category) {
		ArrayList<Coupon> returnedCoupons = new ArrayList<>();
		ArrayList<Integer> customersCoupons = couponRepository.getAllCouponsByCustomersId(customerId);
		List<Coupon> coupons = couponRepository.findAll();
		for (int i = 0; i < customersCoupons.size(); i++) {
				for (int j = 0; j < coupons.size(); j++) {
					//same check but making sure that the category matches
					if(coupons.get(j).getId() == customersCoupons.get(i) &&
							coupons.get(j).getCategory().equals(category)) {
						returnedCoupons.add(coupons.get(j));
					}
				}
		}
		return returnedCoupons;
	}
	
	//getting customer's coupons by max price
	public ArrayList<Coupon> getCustomersCouponsByMaxPrice(double maxPrice) {
		ArrayList<Coupon> returnedCoupons = new ArrayList<>();
		ArrayList<Integer> customersCoupons = couponRepository.getAllCouponsByCustomersId(customerId);
		List<Coupon> coupons = couponRepository.findAll();
		for (int i = 0; i < customersCoupons.size(); i++) {
				for (int j = 0; j < coupons.size(); j++) {
					//same check but making sure that the max price matches
					if(coupons.get(j).getId() == customersCoupons.get(i) &&
						coupons.get(j).getPrice() < maxPrice) {
						returnedCoupons.add(coupons.get(j));
					}
				}
		}
		return returnedCoupons;
	}
	
	//get customer's data
	public Customer getCustomerDetails () throws UnkownCustomerException {
		return customerRepository.findById(customerId).orElseThrow(UnkownCustomerException::new);
	}


	
	

}
