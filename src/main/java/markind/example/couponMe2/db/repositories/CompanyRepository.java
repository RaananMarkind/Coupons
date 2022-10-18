package markind.example.couponMe2.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import markind.example.couponMe2.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
  @Query(value = "SELECT * FROM companies WHERE email=?1 && password=?2"
  , nativeQuery = true)
  Company isCompanyExistsByEmail(String email, String password);
  
  
}

