package twins.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import twins.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {
	
}
