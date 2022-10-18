package markind.example.couponMe2.facades;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.beans.Customer;
import markind.example.couponMe2.db.repositories.CompanyRepository;
import markind.example.couponMe2.db.repositories.CouponRepository;
import markind.example.couponMe2.db.repositories.CustomerRepository;
import markind.example.couponMe2.exceptions.CompanyExistsException;
import markind.example.couponMe2.exceptions.CustomerExsitsException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;

@Service
public class AdminFacade extends ManagerFacade{

	public AdminFacade(CompanyRepository companyRepository, CouponRepository couponRepository,
			CustomerRepository customerRepository) {
		super(companyRepository, couponRepository, customerRepository);
	}

	@Override
	//login for administrator:
	//if information correct, returns 1 or else returns -1
	public int login(String email, String password) throws SQLException {
		if(email.equals("admin@admin.com") && password.equals("admin")) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	//Adding a new company
	public Company addCompany (Company company) throws CompanyExistsException {
		List<Company> companies = getAllCompany();
		for (Company company2 : companies) {
			//checking if company doesn't exists by email or name:
			if(company2.getEmail().equals(company.getEmail()) ||
					company2.getName().equals(company.getName())) {
				throw new CompanyExistsException();
			}
		}
		return companyRepository.save(company);
	}
	
	// updating an existing company
	public Company updateCompany (Company company) throws UnkownCompanyException {
		List<Company> companies = getAllCompany();
		//check if company exists (name and email are unique) 
		for (Company company2 : companies) {
			if(company2.getName().equals(company.getName()) &&
					company2.getEmail().equals(company.getEmail())){
				//make sure the email is not the same to other company
				for (Company company3 : companies) {
					if(company3.getEmail().equals(company.getEmail()) && 
							company3.getId() != company.getId()) {
						throw new UnkownCompanyException();
					}
				}	
				
				return companyRepository.save(company);
			}
		}
		throw new UnkownCompanyException();
	}
	
	//removing a company
	public boolean deleteCompany (int companyId) throws UnkownCompanyException {
		
		List<Company> companies = getAllCompany();
		List<Coupon> coupons = couponRepository.findAll();
		
		for (Company getAllCompanies : companies) {
			if(getAllCompanies.getId() == companyId) {
				for (Coupon coupon : coupons) {
					if (coupon.getCompany().getId() == companyId) {
						//removed more the joined table
						couponRepository.removeByCustomersCouponId(coupon.getId());
//						//after that, remove company's coupons
						couponRepository.removeByCompanyId(companyId);
//						//at the end, remove the company itself
						companyRepository.deleteById(companyId);
						return true;
					}	
				}
			}
		}
		
		throw new UnkownCompanyException();
	}
	
	//returning all the companies
	public List<Company> getAllCompany () {
		return companyRepository.findAll();
	}
	
	//returning one company by the id
	public Company getOneCompany (int companyId) throws UnkownCompanyException {
		return companyRepository.
				findById(companyId).orElseThrow(UnkownCompanyException::new);
	}

	//adding a new customer
	public Customer addCustomer (Customer customer) throws CustomerExsitsException {
		List<Customer> customers = getAllcustomers();
		for (int i = 0; i < customers.size(); i++) {
			//check if customer doesn't exist - email not the same
			if(customers.get(i).getEmail().equals(customer.getEmail().toLowerCase())) {
				throw new CustomerExsitsException();
			}
		}
		return customerRepository.save(customer);
	}
	
	//updating an existing customer
	public Customer updateCustomer(Customer customer) throws CustomerExsitsException {
		List<Customer> customers = getAllcustomers();
		for (Customer customer2 : customers) {
			//check if it's the same customer
			if(customer2.getId() == customer.getId()) {
				return customerRepository.save(customer);
			}
		}
		throw new CustomerExsitsException();
	}
	
	//deleting a customer by id
	public boolean deleteCustomer (int customerId) throws UnkownCustomerException {
		List<Customer> customers = getAllcustomers();
		for (Customer customer : customers) {
			//checking that the customer exists by id
			if(customer.getId() == customerId) {
				customerRepository.deleteById(customerId);
				return true;
			}
		}
		throw new UnkownCustomerException();
	}
	
	//get all customers
	public List<Customer> getAllcustomers () {
		return customerRepository.findAll();
	}
	
	//get a customer by id
	public Customer getOneCustomer(int customerId) throws CustomerExsitsException {
		return customerRepository.
				findById(customerId).orElseThrow(CustomerExsitsException::new);
	}
	
}