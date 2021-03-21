package twins.item;

import twins.user.UserId;

public class CreatedByBoundary {
	private UserId userId;
	
	public CreatedByBoundary() {
		userId=new UserId();
	}

	public CreatedByBoundary(UserId userId) {
		this();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}
}
