package markind.example.couponMe2.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import markind.example.couponMe2.beans.Category;
//import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
//import markind.example.couponMe2.beans.Customer;
import markind.example.couponMe2.beans.Session;
//import markind.example.couponMe2.exceptions.AddCouponException;
//import markind.example.couponMe2.exceptions.BoughtCouponException;
import markind.example.couponMe2.exceptions.InvalidRequestException;
//import markind.example.couponMe2.exceptions.LogisticsCouponException;
//import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;
import markind.example.couponMe2.facades.CustomerFacade;

//Create- none V
//Read- getCustomersCoupons(All(V), Category(V), MaxPrice(V)), getCustomerDetails(V) V
//Update- purchesCoupon (V)
//Delete- none V


@RestController
@RequestMapping(path = "/customer")
@CrossOrigin("*")
public class CustomerController {
	@Autowired
	private HashMap<Long, Session> sessions;
	
	public CustomerController(HashMap<Long, Session> sessions) {
		this.sessions = sessions;
	}
	

	//---------------------------GETS---------------------------\\
	
		@GetMapping(path = "/allcoupons") //v
		public ResponseEntity<?> getAllCoupons(HttpServletRequest request){
			CustomerFacade customerFacade = null;
			try {
				customerFacade = getService(request);
				return ResponseEntity.ok(customerFacade.getCustomersCoupons());
			} catch (InvalidRequestException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		@GetMapping(path = "/catgorycoupons") //v
		public ResponseEntity<?> getAllCouponsByCategory(Category category, HttpServletRequest req){
			CustomerFacade customerFacade = null;
			try {
				customerFacade = getService(req);
				return ResponseEntity.ok(customerFacade.getCustomersCouponsByCategory(category));
			} catch (InvalidRequestException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		@GetMapping(path = "/maxpricecoupons") //v
		public ResponseEntity<?> getAllCouponsByMaxPrice(double maxPrice, HttpServletRequest req){
			CustomerFacade customerFacade = null;
			try {
				customerFacade = getService(req);
				return ResponseEntity.ok(customerFacade.getCustomersCouponsByMaxPrice(maxPrice));
			} catch (InvalidRequestException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		@GetMapping(path = "/info") //v
		public ResponseEntity<?> getCustomersInfo(HttpServletRequest req){
			CustomerFacade customerFacade = null;
			try {
				customerFacade = getService(req);
				return ResponseEntity.ok(customerFacade.getCustomerDetails());
			} catch (InvalidRequestException | UnkownCustomerException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
	
		//---------------------------Put---------------------------\\
//		customerFacade.purchesCoupon(coupon)
		@PutMapping(path = "/buy") //v
		public ResponseEntity<?> purchesCoupon (@RequestBody Coupon coupon, HttpServletRequest req){
			CustomerFacade customerFacade = null;
			try {
				customerFacade = getService(req);
				return ResponseEntity.ok(customerFacade.purchesCoupon(coupon));
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
		
		private CustomerFacade getService(HttpServletRequest req) throws InvalidRequestException {
			String token = req.getHeader("authorization").replace("Bearer ", "");
			Session clientSession = sessions.get(token);
			if(clientSession != null) {
				clientSession.setLastActive(System.currentTimeMillis());
				return (CustomerFacade) clientSession.getFacade();
			}
			else throw new InvalidRequestException("Invalid request");
		}
}
