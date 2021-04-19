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
		rv.setUserId(new UserId(entity.getEmail().split("@@")[1], entity.getEmail().split("@@")[0]));
		rv.setUsername(entity.getUsername());
		return rv;
	}

	@Override
	public UserEntity fromBoundary(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setRole(boundary.getRole());
		
		if(boundary.getAvatar() != null)
			rv.setAvatar(boundary.getAvatar());
		
//		if(boundary.getUserId() != null) {
//			rv.setEmail(boundary.getUserId().getEmail());
//			rv.setSpace(boundary.getUserId().getSpace());
//		}
		
		rv.setUsername(boundary.getUsername());
		return rv;
	}

}
