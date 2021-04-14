package twins.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import twins.data.UserEntity;
import twins.data.UserRole;
import twins.logic.UserEntityConverter;
import twins.logic.UsersService;

//@Service
public class UserServiceMockup implements UsersService {
	
	private Map<String, UserEntity> users;
	private UserEntityConverter userEntityConverter;
	private String springApplicationName;
	
	public UserServiceMockup() {
		//this is a thread safe collection
		this.users=Collections.synchronizedMap(new HashMap<>());
	}
	
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}
	
	@Autowired
	public void setEntityConverter(UserEntityConverter entityConverter) {
		this.userEntityConverter = entityConverter;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		if(user.getAvatar().equals(null) || user.getUsername().equals(null)
			|| user.getUserId().getSpace().equals(null))
			throw new RuntimeException("One of the fields is null");
		if(!checkUserRole(user.getRole()))
			throw new RuntimeException("Role is not one of the enum options");
		if(!checkEmail(user.getUserId().getEmail()))
			throw new RuntimeException("Email is not valid");
		setSpringApplicationName(springApplicationName);
		user.getUserId().setSpace(springApplicationName);
		UserEntity entity = this.userEntityConverter.fromBoundary(user);
		//Doesn't need UUID because each user has different mail.
		String newId= /*UUID.randomUUID().toString()+*/entity.getSpace()+entity.getEmail();
		this.users.put(newId, entity);
		return this.userEntityConverter.toBoundary(entity);
	}

	@Override
	public UserBoundary login(String userSpace, String userEmail) {
		UserEntity entity=null;
		for(Map.Entry<String, UserEntity> current : users.entrySet()) {
			if(current.getValue().getEmail().equals(userEmail) 
					&& current.getValue().getSpace().equals(userSpace)) {
				entity=current.getValue();
				return userEntityConverter.toBoundary(entity);
			}
		}
		//User not found, returned nothing.
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		if(!checkUserRole(update.getRole()))
			throw new RuntimeException("Role is not one of the enum options");
		UserEntity entity=null;
		for(Map.Entry<String, UserEntity> current : users.entrySet()) {
			if(current.getValue().getEmail().equals(userEmail) 
					&& current.getValue().getSpace().equals(userSpace)) {
				entity=userEntityConverter.fromBoundary(update);
				entity.setEmail(current.getValue().getEmail());
				entity.setSpace(current.getValue().getSpace());
				current.setValue(entity);
				break;
			}
		}
		if(entity==null)
			throw new RuntimeException("The requested user doesn't exist");
		return userEntityConverter.toBoundary(entity);
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		return this.users.values().stream().map(this.userEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		this.users.clear();
	}
	
	public boolean checkEmail(String email) {
		if(email.equals(null))
			return false;
		String[] splitted=email.split("@");
		int size1=splitted.length;
		if(size1>2 || size1<1)
			return false;
		int size2=splitted[1].split("//.").length;
		if(size2>3 || size2<1)
			return false;
		return true;
	}
	
	public boolean checkUserRole(String userRole) {
		if(userRole.equals(null))
			return false;
		for(UserRole role : UserRole.values()) {
			if(userRole.equals(role.toString()))
				return true;
		}
		return false;
	}

}
