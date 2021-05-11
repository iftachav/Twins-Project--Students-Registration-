package twins.logic;

import java.util.List;

import twins.user.UserBoundary;

public interface UpdatedUsersService extends UsersService {
	
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail,int size, int page);

}
