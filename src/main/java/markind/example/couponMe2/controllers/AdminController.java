package markind.example.couponMe2.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import markind.example.couponMe2.beans.Company;
import markind.example.couponMe2.beans.Coupon;
import markind.example.couponMe2.beans.Customer;
import markind.example.couponMe2.beans.Session;
import markind.example.couponMe2.exceptions.AddCouponException;
import markind.example.couponMe2.exceptions.CompanyExistsException;
import markind.example.couponMe2.exceptions.CustomerExsitsException;
import markind.example.couponMe2.exceptions.InvalidRequestException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;
import markind.example.couponMe2.facades.AdminFacade;
import markind.example.couponMe2.facades.CompanyFacade;

//Create- addCompany(V), addCustomer(V)
//Read- getAllCompany(V), getAllcustomers(V), getOneCustomer(V), getOneCompany(V)
//Update- updateCompany(V), updateCustomer(V)
//Delete- deleteCompany(V), deleteCustomer(V)

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/admin")
public class AdminController {

	@Autowired
	private HashMap<Long, Session> sessions;
	
	public AdminController(HashMap<Long, Session> sessions) {
		super();
		this.sessions = sessions;
	}

	//---------------------------POSTS---------------------------\\
	
	@PostMapping(path = "/addcompany") //v
	public ResponseEntity<?> addCompany(@RequestBody Company company, HttpServletRequest request) {
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.addCompany(company));
		} catch (InvalidRequestException | CompanyExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping(path = "/addcustomer") //v
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer, HttpServletRequest request) {
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.addCustomer(customer));
		} catch (InvalidRequestException | CustomerExsitsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//---------------------------GETS---------------------------\\
	
	@GetMapping(path = "/companies") //v
	public ResponseEntity<?> getAllCompanies(HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.getAllCompany());
		} catch (InvalidRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
 	
	@GetMapping(path = "/customers") //v
	public ResponseEntity<?> getAllCustomers(HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.getAllcustomers());
		} catch (InvalidRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(path = "/customer/{id}") //vs
	public ResponseEntity<?> getCustomer(@PathVariable int id, HttpServletRequest request) {
		
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.getOneCustomer(id));
		} catch (InvalidRequestException | CustomerExsitsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping(path = "/company/{id}") //v
	public ResponseEntity<?> getCompany(@PathVariable int id, HttpServletRequest request) {
		
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.getOneCompany(id));
		} catch (InvalidRequestException | UnkownCompanyException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	//---------------------------Put---------------------------\\
	
	@PutMapping(path = "/update/comp") //v
	public ResponseEntity<?> updateCompany (@RequestBody Company company, HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.updateCompany(company));
		} catch (InvalidRequestException | UnkownCompanyException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/update/cust") //v
	public ResponseEntity<?> updateCustomer (@RequestBody Customer customer, HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			return ResponseEntity.ok(adminFacade.updateCustomer(customer));
		} catch (InvalidRequestException | CustomerExsitsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//---------------------------Delete---------------------------\\
	
	@DeleteMapping(path = "/compld/{id}") //v
    public ResponseEntity<String> deleteCompany(@PathVariable int id, HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			if(adminFacade.deleteCompany(id)) {
				return ResponseEntity.ok("An Employee with the Id "+id+" has been delted");
			}
		} catch (UnkownCompanyException | InvalidRequestException e) {}
		return ResponseEntity.badRequest().body("Employee with the id "+id+" was not found"); 
    }
	
	@DeleteMapping(path = "/custd/{id}") //v
    public ResponseEntity<String> deleteCustomer(@PathVariable int id, HttpServletRequest request){
		AdminFacade adminFacade = null;
		try {
			adminFacade = getService(request);
			if(adminFacade.deleteCustomer(id)) {
				return ResponseEntity.ok("A Customer with the Id "+id+" has been delted");
			}
		} catch (UnkownCustomerException | InvalidRequestException e) {}
		return ResponseEntity.badRequest().body("Customer with the id "+id+" was not found"); 
    }
	
	private AdminFacade getService(HttpServletRequest req) throws InvalidRequestException {
		String token = req.getHeader("authorization").replace("Bearer ", "");
		Session clientSession = sessions.get(token);
		if(clientSession != null) {
			clientSession.setLastActive(System.currentTimeMillis());
			return (AdminFacade) clientSession.getFacade();
		}
		else throw new InvalidRequestException("Invalid request");
	}
}
