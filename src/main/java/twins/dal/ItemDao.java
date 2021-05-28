package twins.dal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.ItemEntity;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String>{
	public Optional<ItemEntity> findByIdAndActive(@Param("itemId") String id, @Param("active") boolean active);
	
	public List<ItemEntity> findAllByType(
			@Param("type") String type,
			Sort sort);
	
	public List<ItemEntity> findAllByTypeAndActive(
			@Param("type") String type,
			@Param("active") boolean active,
			Sort sort);
	
	public List<ItemEntity> findAllByNameAndType(
			@Param("name") String courseCode,
			@Param("type") String type,
			Sort sort);
	
	public List<ItemEntity> findAllByNameAndTypeAndActive(
			@Param("name") String courseCode,
			@Param("type") String type,
			@Param("active") boolean active, 
			Sort sort);
	
	public List<ItemEntity> findAllStudentsByChildren_name(@Param("children_name") String children_name);
	
	public List<ItemEntity> findAllStudentsByChildren_nameAndChildren_active(@Param("children_name") String children_name, @Param("children_active") boolean children_active);
	
	public Optional<ItemEntity> findLecturerByTypeAndActiveAndParents_id(@Param("type") String type,@Param("active") boolean active, @Param("parent_id") String parents_id);
	
	public Optional<ItemEntity> findStudentByNameAndParents_id(@Param("name") String name, @Param("parent_id") String parents_id);
}
