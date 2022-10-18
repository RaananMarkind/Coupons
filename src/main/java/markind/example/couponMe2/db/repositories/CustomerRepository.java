package markind.example.couponMe2.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import markind.example.couponMe2.beans.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	  @Query(value = "SELECT * FROM customers WHERE email=?1 && password=?2"
			  , nativeQuery = true)
			  Customer isCustomerExistsByEmail(String email, String password);
}
