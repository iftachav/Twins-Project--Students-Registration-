package twins.dal;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.ItemEntity;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String>{
	public List<ItemEntity> findAllById(@Param("itemId") String id);
	
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
	
//	@Query("from Item item left join fetch item.children students where students.name = student_id")
//	public List<ItemEntity> findRegisteredCourses(@Param("student_id") String student_id);
}
