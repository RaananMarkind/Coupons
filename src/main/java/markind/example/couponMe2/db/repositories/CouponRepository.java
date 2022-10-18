package markind.example.couponMe2.db.repositories;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	//is coupon exists by id:
	@Query(value = "SELECT * FROM coupons WHERE id=?1 && company_id=?2"
	, nativeQuery = true)
	Coupon isCoupobExistsById(int id, int companyId);
	
	//get coupon by customers and coupon id
	@Query(value = "SELECT * FROM customers_coupons WHERE customer_id=?1 && coupons_id=?2"
			, nativeQuery = true)
			Coupon isCoupobExistsByIdConnector(int customerId, int couponsId);
	
	//gets the id of customers coupons
	@Query(value = "select coupons_id from customers_coupons where customer_id=?1"
			, nativeQuery = true)
			ArrayList<Integer> getAllCouponsByCustomersId(int customerId);
	
	//adding a coupon to customer
	@Transactional
	@Modifying
	@Query(value = "insert into customers_coupons values (?1, ?2);"
			, nativeQuery = true)
			void insertCouponsToCustomer(int customerId, int couponsId);
	
	//remove customer's coupon by the coupon id
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM customers_coupons WHERE coupons_id=?1"
			, nativeQuery = true)
		void removeByCustomersCouponId(int couponsId);
	
	
	//removing company's coupon
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM coupons WHERE company_id=?1"
			, nativeQuery = true)
		void removeByCompanyId(int companyId);
}
