package markind.example.couponMe2;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Set;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import markind.example.couponMe2.beans.Category;
import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.beans.Customer;
import markind.example.couponMe2.beans.Session;
import markind.example.couponMe2.exceptions.AddCouponException;
import markind.example.couponMe2.exceptions.BoughtCouponException;
import markind.example.couponMe2.exceptions.CompanyExistsException;
import markind.example.couponMe2.exceptions.CustomerExsitsException;
import markind.example.couponMe2.exceptions.LoginException;
import markind.example.couponMe2.exceptions.LogisticsCouponException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCouponException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;
import markind.example.couponMe2.facades.AdminFacade;
import markind.example.couponMe2.facades.CompanyFacade;
import markind.example.couponMe2.facades.CustomerFacade;
import markind.example.couponMe2.facades.ManagerFacade;
import markind.example.couponMe2.job.ExpiredCouponJob;
import markind.example.couponMe2.loginManager.ClientType;
import markind.example.couponMe2.loginManager.LoginManager;

/**
 * @author Raanan Markind
 *
 */
@SpringBootApplication
public class CouponMe2Application {

	public static void main(String[] args) {
		
//		Thread thread = new Thread(new ExpiredCouponJob());
//		thread.start();
		
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponMe2Application.class, args);
		LoginManager loginManager = ctx.getBean(LoginManager.class);
		
//		//testing
//		//--------------------------------TESTING FACADES !!!!-------------------\\		
//		try {
//			//------------Testing Administration facade------------\\
//			System.out.println("------------Testing Admin facade now------------");
//			
//			//Checking login:
//			System.out.println("Check login:");
//			
//			//old check without login manager (irrelevant):
//			//System.out.println(adminTester.login("raanan", "123a")); // prints -1 - wrong.
//			//System.out.println(adminTester.login("admin@admin.com", "admin")); // prints 1 - okay
//			//new check with login manager:
//			AdminFacade adminTester =  (AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
//
//			//adding a company
//			System.out.println("adding a new company: ");
//			adminTester.addCompany(new Company("dora2", "dora2@gmail.com", "bbabb123"));
//			System.out.println("Trying to add the same company");
//			adminTester.addCompany(new Company("Cola", "cola123@gmail.com", "aabb123"));
//			
//			//update Company
//			Company company1 = adminTester.getOneCompany(2);
//			company1.setEmail("cola123@gmail.com");
//			adminTester.updateCompany(company1);
//			
//			//delete company - *********************************check it later
//			System.out.println("Deleting a company: ");
//			adminTester.deleteCompany(3);
//			
//			//find one company
//			System.out.println("Getting one company: ");
//			System.out.println(adminTester.getOneCompany(2));
//			
//			//get all companies
//			System.out.println("Getting all companies: ");
//			System.out.println(adminTester.getAllCompany());
//			
//			//add a new customer
//			System.out.println("Adding a new customer: ");
//			adminTester.addCustomer(new Customer("maya", "choen", "add@gmail.com", "abb123"));
//			
//			//Update a customer
//			Customer customer1 = adminTester.getOneCustomer(1);
//			adminTester.updateCustomer(customer1);
//			
//			//Delete a customer
//			System.out.println("deleting a customer");
//			adminTester.deleteCustomer(1);
//			
//			//Get one customer
//			System.out.println("getting one customer");
//			System.out.println(adminTester.getOneCustomer(2));
//		}catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//		
//		
//		
		try {
//			//------------Testing Company facade------------\\
//			System.out.println("------------Testing Company facade now------------");
//			
//			//old check without login manager (irrelevant):
////			CompanyFacade companyTester = ctx.getBean(CompanyFacade.class);
//			
//			//checking login
//			//new check with login manager:
//			System.out.println("Checking login: ");
			CompanyFacade companyTester = (CompanyFacade) loginManager.login("cola123@gmail.com", "aabb123", ClientType.COMPANY);
//			System.out.println(companyTester.login("shane@gmail.com", "bbbb123"));
//			
//			//Adding a new coupon to a company
//			System.out.println("Adding a new company: ");
//			Company company2 = companyTester.getCompanyDetails();
//			LocalDate date1 = LocalDate.of(2012,Month.JUNE , 16);
//			LocalDate date2 = LocalDate.of(2230, Month.APRIL, 05);
//			companyTester.addCoupon(new Coupon
//					(company2, Category.VACATIONS, "Italia", "pizza", date1, date2, 25, 10.6, "cheese.jpg"));
//			
//			//get one company details:
//			System.out.println("Getting companys details");
//			System.out.println(companyTester.getCompanyDetails());
//			
//			//update company's coupon:
//			System.out.println("Updating a coupon: ");
//			LocalDate dateStart = LocalDate.of(2012, 02, 05);
//			LocalDate dateEnd = LocalDate.of(2013, 03, 06);
//			Coupon coupon = new Coupon(2 ,Category.FOOD, "blah", "blah", dateStart, dateEnd, 12, 25, "asd");
//			companyTester.updateCoupon(coupon);
//			
//			//delete company's coupon:
//			System.out.println("Deleting companys' coupon");
//			companyTester.deleteCoupon(4);
//			
//			//get all coupons:
//			System.out.println("Getting all of the coupons:");
//			System.out.println(companyTester.getCompanyCoupons());
//			
//			//get all coupons by categroy
//			System.out.println("Get all coupons by category: ");
//			System.out.println(companyTester.getCompanyCouponsByCategory(Category.FOOD).size());
//			
//			//get all coupons by price:
//			System.out.println("Get all coupons by max price: ");
//			System.out.println(companyTester.getCompanyCouponsByPrice(55));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
//		try {
//			//------------Testing Customer facade------------\\
//			System.out.println("------------Testing Company facade now------------");
////			CustomerFacade customerTester = ctx.getBean(CustomerFacade.class);
//			
////			System.out.println("Testing login for customer: ");
//			CustomerFacade customerTester = (CustomerFacade) loginManager.login("asd@asd", "asd123", ClientType.CUSTOMER);
//			System.out.println(customerTester.login("b@b", "123123"));
//			
//			System.out.println("Getting data of a customer: ");
//			System.out.println(customerTester.getCustomerDetails());
//			
//			System.out.println("Adding coupon to customer");
//			LocalDate dateStart2 = LocalDate.of(2012, 02, 05);
//			LocalDate dateEnd2 = LocalDate.of(2013, 03, 06);
//			Coupon coupon3 = new Coupon(
//					Category.VACATIONS, "Italia", "pizza", dateStart2, dateEnd2, 25, 10.6, "cheese.jpg");
//			customerTester.purchesCoupon(coupon3);
//			
//			//get customers coupons:
//			System.out.println("Getting all the coupons of the customer: ");
//			System.out.println(customerTester.getCustomersCoupons());
//			
//			//get customers coupons by category:
//			System.out.println("Getting all the coupons of the customer by catgory: ");
//			System.out.println(customerTester.getCustomersCouponsByCategory(Category.FOOD));
//
//			//get customers coupons by maximum price:
//			System.out.println("Getting all the coupons of the customer by maximum price: ");
//			System.out.println(customerTester.getCustomersCouponsByMaxPrice(21));
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
		
	}
	
	@Bean
	public HashMap<Long, Session> sessions(){
		return new HashMap<Long, Session>();
	}
}
