package twins.user;

/*
 UserBoundary sample JSON: 
	{ 
	"userId":{ 
		"space":"2021b.iftach.avraham", 
		"email":"user@demo.com" 
		}, 
	"role":"MANAGER", 
	"username":"Demo User", 
	"avatar":"J" 
	} 

 */
public class UserBoundary {
	private UserId userId;
	private String role;
	private String username;
	private String avatar;
	
	public UserBoundary() {
		this.userId = new UserId();
	}
	
	public UserBoundary(String role, String username, String avatar) {
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}
	
	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBoundary other = (UserBoundary) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
}
