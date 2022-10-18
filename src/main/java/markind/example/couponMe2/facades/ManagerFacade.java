package markind.example.couponMe2.facades;

import java.sql.SQLException;

import markind.example.couponMe2.db.repositories.CompanyRepository;
import markind.example.couponMe2.db.repositories.CouponRepository;
import markind.example.couponMe2.db.repositories.CustomerRepository;

public abstract class ManagerFacade {
	protected CompanyRepository companyRepository;
	protected CouponRepository couponRepository;
	protected CustomerRepository customerRepository;
	
	public ManagerFacade(CompanyRepository companyRepository, CouponRepository couponRepository,
			CustomerRepository customerRepository) {
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;
	}



	public abstract int login(String email, String password) throws SQLException;
}
