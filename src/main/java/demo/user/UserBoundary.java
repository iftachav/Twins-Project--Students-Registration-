package demo.user;

/*
 UserBoundary sample JSON: 
	{ 
	"userId":{ 
		"space":"2021b.twins", 
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
		this.role = "MANAGER";
		this.username = "Demo User";
		this.avatar = "J";
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
	
	
	
}
