package demo.operations;

import demo.user.UserId;

public class UserIdWrapper {
	private UserId userId;
	
	public UserIdWrapper() {
		this.userId = new UserId();
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	
	
}
