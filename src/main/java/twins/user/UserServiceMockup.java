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
import twins.logic.UserEntityConverter;
import twins.logic.UserService;

@Service
public class UserServiceMockup implements UserService {
	
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
		//TODO need to read the instructions and change if needed.
		UserEntity entity = this.userEntityConverter.fromBoundary(user);
		String newId= UUID.randomUUID().toString();
		this.users.put(newId, entity);
		return this.userEntityConverter.toBoundary(entity);
	}

	@Override
	public UserBoundary login(String userSpace, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		//TODO tried to get the specific user and to get his key - for updating him with the new one.
//		this.users.values().stream().map(e ->{
//			if(e.getEmail().equals(userEmail) && e.getSpace().equals(userSpace)) {
//				users.values();
//				
//			}
//		})
		
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		return this.users
				.values()
				.stream()
				.map(this.userEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		this.users.clear();
	}

}
