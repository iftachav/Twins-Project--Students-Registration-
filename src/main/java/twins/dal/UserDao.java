package twins.dal;

import org.springframework.data.repository.CrudRepository;

import twins.data.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, String> {

}
