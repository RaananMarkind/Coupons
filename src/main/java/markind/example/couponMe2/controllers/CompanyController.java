package markind.example.couponMe2.controllers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import markind.example.couponMe2.beans.Category;
import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.beans.Session;
import markind.example.couponMe2.exceptions.AddCouponException;
import markind.example.couponMe2.exceptions.InvalidRequestException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCouponException;
import markind.example.couponMe2.facades.AdminFacade;
import markind.example.couponMe2.facades.CompanyFacade;
import markind.example.couponMe2.facades.CustomerFacade;

//Create- add coupon V
//Read- getCompanyCoupons(Category(V), Price(V), Total(V)) V
//Update- updateCoupon(V) 
//Delete- deleteCoupon(V)

@RestController
@RequestMapping(path = "/company")
@CrossOrigin("*")
public class CompanyController {
	@Autowired
	private HashMap<Long, Session> sessions;
	
	public CompanyController(HashMap<Long, Session> sessions) {
		super();
		this.sessions = sessions;
	}
	
	
	//---------------------------POSTS---------------------------\\

	@PostMapping //v
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, HttpServletRequest request) {
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.addCoupon(coupon));
		} catch (InvalidRequestException | AddCouponException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//---------------------------GETS---------------------------\\
	
	//Ask Nir if it's okay to use @jsonIgnore as a solution for the outcome
	//try to use PostMan without jsonIgnore in the Company bean.
	
	@GetMapping(path = "/allcoupons") //v
	public ResponseEntity<?> getCompanysCoupons (HttpServletRequest request){
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.getCompanyCoupons());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(path = "/pricecoupons") //v
	public ResponseEntity<?> getCompanysCouponsByPrice (double maxPrice, HttpServletRequest request){
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.getCompanyCouponsByPrice(maxPrice));
		} catch (InvalidRequestException | UnkownCouponException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(path = "/categorycoupons") //v
	public ResponseEntity<?> getCompanysCouponsByCategory (Category category, HttpServletRequest request){
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.getCompanyCouponsByCategory(category));
		} catch (InvalidRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	//---------------------------Put---------------------------\\
	
	@PutMapping(path = "/update/coupon") //v
	public ResponseEntity<?> updateCoupon (@RequestBody Coupon coupon, HttpServletRequest request){
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.updateCoupon(coupon));
		} catch (UnkownCouponException | InvalidRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//---------------------------Delete---------------------------\\
	
	@DeleteMapping(path = "/coupondel/{id}") //v
    public ResponseEntity<?> deleteCoupon(@PathVariable int id, HttpServletRequest request){
		CompanyFacade companyFacade = null;
		try {
			companyFacade = getService(request);
			return ResponseEntity.ok(companyFacade.deleteCoupon(id));
		} catch (InvalidRequestException | UnkownCouponException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
	
	private CompanyFacade getService(HttpServletRequest req) throws InvalidRequestException {
		String token = req.getHeader("authorization").replace("Bearer ", "");
		Session clientSession = sessions.get(token);
		if(clientSession != null) {
			clientSession.setLastActive(System.currentTimeMillis());
			return (CompanyFacade) clientSession.getFacade();
		}
		else throw new InvalidRequestException("Invalid request");
	}
	
}
