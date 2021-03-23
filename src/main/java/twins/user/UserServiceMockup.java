package twins.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import twins.data.UserEntity;
import twins.data.UserRole;
import twins.logic.UserEntityConverter;
import twins.logic.UsersService;

@Service
public class UserServiceMockup implements UsersService {
	
	private Map<String, UserEntity> users;
	private UserEntityConverter userEntityConverter;
	
	public UserServiceMockup() {
		//this is a thread safe collection
		this.users=Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setEntityConverter(UserEntityConverter entityConverter) {
		this.userEntityConverter = entityConverter;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		if(!checkUserRole(user.getRole()))
			throw new RuntimeException("Role is not one of the enum options");
		if(!checkEmail(user.getUserId().getEmail()))
			throw new RuntimeException("Email is not valid");
		//TODO need to use spring.application.name.
		user.getUserId().setSpace("2021b.iftach.avraham");
		UserEntity entity = this.userEntityConverter.fromBoundary(user);
		String newId= UUID.randomUUID().toString();
		this.users.put(newId, entity);
		return this.userEntityConverter.toBoundary(entity);
	}

	@Override
	public UserBoundary login(String userSpace, String userEmail) {
//		TODO thats it?
		UserEntity entity=null;
		for(Map.Entry<String, UserEntity> current : users.entrySet()) {
			if(current.getValue().getEmail().equals(userEmail) 
					&& current.getValue().getSpace().equals(userSpace)) {
				entity=current.getValue();
				break;
			}
		}
		if(entity==null)
			throw new RuntimeException("The requested user doesn't exist");
		return userEntityConverter.toBoundary(entity);
	}

	@Override
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		if(!checkUserRole(update.getRole()))
			throw new RuntimeException("Role is not one of the enum options");
		UserEntity entity=null;
//		TODO if the search by the space and mail and not from update.
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
		for(UserRole role : UserRole.values()) {
			if(userRole.equals(role.toString()))
				return true;
		}
		return false;
	}

}
