package markind.example.couponMe2.loginManager;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import markind.example.couponMe2.exceptions.LoginException;
import markind.example.couponMe2.facades.AdminFacade;
import markind.example.couponMe2.facades.CompanyFacade;
import markind.example.couponMe2.facades.CustomerFacade;
import markind.example.couponMe2.facades.ManagerFacade;

@Service
public class LoginManager {
	
	@Autowired
	private ApplicationContext instance;

	
	public ManagerFacade login (String email, String password, ClientType clientType) throws LoginException, SQLException {
		switch (clientType) {
		
		case ADMINISTRATOR:
			AdminFacade adminFacade = instance.getBean(AdminFacade.class);
			if(adminFacade.login(email, password) == -1) {
				throw new LoginException();
			}
			return adminFacade;
		
		case COMPANY:
			CompanyFacade companyFacade = instance.getBean(CompanyFacade.class);
			if(companyFacade.login(email, password) == -1) {
				throw new LoginException();
			}
			return companyFacade;
		
		case CUSTOMER:
			CustomerFacade customerFacade = instance.getBean(CustomerFacade.class);
			if(customerFacade.login(email, password) == -1) {
				throw new LoginException();
			}
			return customerFacade;
		}
		return null;
	}
}
