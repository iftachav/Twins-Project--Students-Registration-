package twins.item;

import org.springframework.data.repository.CrudRepository;

import twins.data.ItemEntity;

public interface ItemDao extends CrudRepository<ItemEntity, String>{

}
