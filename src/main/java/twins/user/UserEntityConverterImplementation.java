package twins.user;

import org.springframework.stereotype.Component;

import twins.data.UserEntity;
import twins.logic.UserEntityConverter;

@Component
public class UserEntityConverterImplementation implements UserEntityConverter {

	@Override
	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary rv= new UserBoundary();
		rv.setAvatar(entity.getAvatar());
		rv.setRole(entity.getRole());
		rv.setUserId(new UserId(entity.getEmailSpace().split("@@")[1], entity.getEmailSpace().split("@@")[0]));
		rv.setUsername(entity.getUsername());
		return rv;
	}

	@Override
	public UserEntity fromBoundary(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setRole(boundary.getRole());
		
		if(boundary.getAvatar() != null)
			rv.setAvatar(boundary.getAvatar());
		
		rv.setUsername(boundary.getUsername());
		return rv;
	}

}
