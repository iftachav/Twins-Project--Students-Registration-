package twins.dal;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {
	Optional<UserEntity> findFirstByEmailSpace(@Param("emailAndSpace") String emailAndSpace);
}
