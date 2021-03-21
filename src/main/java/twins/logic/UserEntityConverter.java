package twins.logic;

import twins.data.UserEntity;
import twins.user.UserBoundary;

public interface UserEntityConverter {
	
	public UserBoundary toBoundary(UserEntity entity);

	public UserEntity fromBoundary(UserBoundary boundary);

}
