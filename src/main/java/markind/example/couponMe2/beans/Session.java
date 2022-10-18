package markind.example.couponMe2.beans;

import markind.example.couponMe2.facades.ManagerFacade;

public class Session {
	private ManagerFacade facade;
	private long lastActive;
	
	public Session(ManagerFacade facade, long lastActive) {
		super();
		this.facade = facade;
		this.lastActive = lastActive;
	}

	public ManagerFacade getFacade() {
		return facade;
	}

	public void setFacade(ManagerFacade facade) {
		this.facade = facade;
	}

	public long getLastActive() {
		return lastActive;
	}

	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}
}
