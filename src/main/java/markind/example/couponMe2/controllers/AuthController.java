package markind.example.couponMe2.controllers;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import markind.example.couponMe2.beans.Session;
import markind.example.couponMe2.exceptions.LoginException;
import markind.example.couponMe2.exceptions.UnkownCompanyException;
import markind.example.couponMe2.exceptions.UnkownCustomerException;
import markind.example.couponMe2.facades.AdminFacade;
import markind.example.couponMe2.facades.CompanyFacade;
import markind.example.couponMe2.facades.CustomerFacade;
import markind.example.couponMe2.facades.ManagerFacade;
import markind.example.couponMe2.loginManager.ClientType;
import markind.example.couponMe2.loginManager.LoginManager;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	
	private LoginManager loginManager;
	private HashMap<Long, Session> sessions;
	
	public AuthController(LoginManager loginManager, HashMap<Long, Session> sessions) {
		super();
		this.loginManager = loginManager;
		this.sessions = sessions;
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login (String email, String password, String clientType) {
		ClientType type = ClientType.valueOf(clientType);
		System.out.println(type);
		try {
			ManagerFacade facade = loginManager.login(email, password, type);
			long id = 0;
			
			if(facade instanceof CustomerFacade) {
				id = ((CustomerFacade) facade).getCustomerDetails().getId();
			}
			else if (facade instanceof CompanyFacade) {
				id = ((CompanyFacade) facade).getCompanyDetails().getId();
			}
			else if (facade instanceof AdminFacade) {
				id = 1;
			}
			String token = createToken(email, id, type);
			sessions.put(id, new Session(facade, System.currentTimeMillis()));
			
			return ResponseEntity.ok(token);
		} catch (LoginException | SQLException | UnkownCustomerException | UnkownCompanyException e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}
	
	private String createToken(String email, long id, ClientType clientType) {
		String token = JWT.create()
				.withIssuer("Raanan")
				.withIssuedAt(new Date())
				.withClaim("id", id)
				.withClaim("email", email)
				.withClaim("clientType", clientType.toString())
				.sign(Algorithm.HMAC256("somecoolstringoverherebro"));
		return token;
	}
	
}
