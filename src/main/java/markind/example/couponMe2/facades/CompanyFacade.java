package markind.example.couponMe2.facades;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import markind.example.couponMe2.beans.Category;
import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.db.repositories.CompanyRepository;
import markind.example.couponMe2.db.repositories.CouponRepository;
import markind.example.couponMe2.db.repositories.CustomerRepository;
import markind.example.couponMe2.exceptions.AddCouponException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCouponException;

@Service
@Scope("prototype")
public class CompanyFacade extends ManagerFacade {
	private int companyId;
	
	public CompanyFacade(CompanyRepository companyRepository, CouponRepository couponRepository,
			CustomerRepository customerRepository) {
		super(companyRepository, couponRepository, customerRepository);
	}

	@Override
	//login
	public int login(String email, String password) throws SQLException {
		companyId = -99;
		//checking that the login is correct and setting the id if so
		if(companyRepository.isCompanyExistsByEmail(email, password) != null) {
			companyId = companyRepository.isCompanyExistsByEmail(email, password).getId();
			return 1;
		}
		return -1;
	}
	//company adds a new coupon
	public Coupon addCoupon (Coupon coupon) throws AddCouponException {
		List<Coupon> coupons = couponRepository.findAll();
		for (Coupon coupon2 : coupons) {
			//same title not allowed to the same company
			//checking if coupon is not expired
			//checking if there is enough of the coupon
			if(coupon.getTitle().equals(coupon2.getTitle()) && 
					companyId == coupon2.getId() && coupon.getAmount() <= 0 &&
					coupon.getEndDate().isAfter(LocalDate.now())){
						throw new AddCouponException();
			}
		}	
		couponRepository.save(coupon);
		return coupon;
	}
	
	//updating an existing company's coupon
	public Coupon updateCoupon(Coupon coupon) throws UnkownCouponException {
		//coupon id can't be updated
		//can't update company's id
		//check coupon id exists
		if(couponRepository.isCoupobExistsById(coupon.getId(), companyId) !=null) {
			return couponRepository.save(coupon);
		}
		else {
			throw new UnkownCouponException();	
		}		
	}
	
	//company deletes her coupon
	public boolean deleteCoupon (int couponId) throws UnkownCouponException {
		//check if coupon exists by id
		if(couponRepository.isCoupobExistsById(couponId, companyId) !=null) {
			//delete in the joined table (couponsXcustomer).
			couponRepository.removeByCustomersCouponId(couponId);
			//removes from the coupons
			couponRepository.deleteById(couponId);
			return true;
		}
		else {
			throw new UnkownCouponException();
		}
	}
	
	//get all company's coupons
	public ArrayList<Coupon> getCompanyCoupons(){
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) couponRepository.findAll();
		ArrayList<Coupon> returnCoupons = new ArrayList<>();
		for (int i = 0; i < coupons.size(); i++) {
			//checking that the coupons belongs to the company
			if(coupons.get(i).getCompany().getId() == companyId) {
				returnCoupons.add(coupons.get(i));
			}
		}
		return returnCoupons;
	}
	
	//getting all the coupons of the company by the category
	public ArrayList<Coupon> getCompanyCouponsByCategory(Category category){
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) couponRepository.findAll();
		ArrayList<Coupon> returnCoupons = new ArrayList<>();
		for (int i = 0; i < coupons.size(); i++) {
			if(coupons.get(i).getCategory().equals(category) &&
					coupons.get(i).getCompany().getId() == companyId) {
				returnCoupons.add(coupons.get(i));
			}
		}
		return returnCoupons;
	}
	//getting all the coupons of the company by the price
	public ArrayList<Coupon> getCompanyCouponsByPrice(double maxPrice) throws UnkownCouponException{
		//make sure the input is okay
		if(maxPrice <= 0) {
			throw new UnkownCouponException();
		}
		ArrayList<Coupon> coupons = (ArrayList<Coupon>) couponRepository.findAll();
		ArrayList<Coupon> returnCoupons = new ArrayList<>();
		for (int i = 0; i < coupons.size(); i++) {
			//getting all the coupons that belongs to the company 
			//in the range of the price
			if(coupons.get(i).getPrice() <= maxPrice &&
					coupons.get(i).getCompany().getId() == companyId) {
				returnCoupons.add(coupons.get(i));
			}
		}
		return returnCoupons;
	}
	
	
	//getting the comapny's details
	public Company getCompanyDetails() throws UnkownCompanyException {
		return companyRepository.
				findById(companyId).orElseThrow(UnkownCompanyException::new);
	}
}
