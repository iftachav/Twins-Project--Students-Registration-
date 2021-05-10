package twins.dal;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.ItemEntity;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String>{
	public List<ItemEntity> findAllById(@Param("itemId") String id);
	
	public List<ItemEntity> findAllByUserEmailAndSpace(@Param("userEmail") String userEmail, @Param("space") String space);
}
