package twins.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.data.UserDao;
import twins.data.UserEntity;
import twins.data.UserRole;
import twins.logic.UserEntityConverter;
import twins.logic.UsersService;

@Service
public class UserServiceJPA implements UsersService{
	private UserDao userDao;
	private UserEntityConverter userEntityConverter;
	private String springApplicationName;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
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
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		if(user.getAvatar() == null)
			throw new RuntimeException("User avatar can't be null");
		if(user.getUsername() == null)
			throw new RuntimeException("Username can't be null");
		if(!checkUserRole(user.getRole()) || user.getRole() == null)
			throw new RuntimeException("Invalid Role");
		if(!checkEmail(user.getUserId().getEmail()))
			throw new RuntimeException("Invalid Email");
		if(user.getUserId().getSpace() == null)
			user.getUserId().setSpace(springApplicationName);
		
		UserEntity entity = this.userEntityConverter.fromBoundary(user);		
		//String userId = entity.getSpace() + entity.getEmail();
		userDao.save(entity);
		return this.userEntityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSpace, String userEmail) {
		if(!userSpace.equals(this.springApplicationName))
			throw new RuntimeException("User: " + userEmail + " doesn't exist in current space " + springApplicationName);
			
		Optional<UserEntity> optionalUser = this.userDao.findById(userEmail);
		
		if(!optionalUser.isPresent())
			throw new RuntimeException("User: " + userEmail + " doesn't exist");
		
		UserEntity userEntity = optionalUser.get();
		return userEntityConverter.toBoundary(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		if(!checkUserRole(update.getRole()))
			throw new RuntimeException("Invalid Role");
		
		Optional<UserEntity> optionalUser = this.userDao.findById(userEmail);
		if(!optionalUser.isPresent())
			throw new RuntimeException("User: " + userEmail + " doesn't exist");
		
		UserEntity user = optionalUser.get();		
		
		if(update.getAvatar() != null)
			user.setAvatar(update.getAvatar());
		if(update.getUsername() != null)
			user.setUsername(update.getUsername());
		if(user.getRole() != null)
			user.setRole(user.getRole());

		user = userDao.save(user);
		return userEntityConverter.toBoundary(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		Iterable<UserEntity> users = this.userDao.findAll();
		return StreamSupport.stream(users.spliterator(), false).map(this.userEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		this.userDao.deleteAll();
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
		for(UserRole role : UserRole.values()) {
			if(userRole.equals(role.toString()))
				return true;
		}
		return false;
	}

}
