package twins.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//	EMAIL	@@	SPACE	|	ROLE	|	USER_NAME	|	AVATAR
//	VARCHAR				|	VARCHAR	|	VARCHAR		| 	VARCHAR
//	<PK>
@Entity
@Table(name="USERS")
public class UserEntity {
	private String emailAndSpace;	//email: email@@space
	private String role;
	private String username;
	private String avatar;
	
	public UserEntity() {
	}
	
	@Id
	public String getEmailAndSpace() {
		return emailAndSpace;
	}

	public void setEmailAndSpace(String email) {
		this.emailAndSpace = email;
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
	
//	public String getEmail() {
//		return this.emailAndSpace.split("@@")[0];
//	}
//	
//	public String getSpace() {
//		return this.emailAndSpace.split("@@")[1];
//	}
	
}
