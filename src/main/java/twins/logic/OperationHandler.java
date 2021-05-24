package twins.logic;

import java.util.List;

import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.item.ItemBoundary;

public interface OperationHandler {
	public void registerToCourse(OperationEntity operation, ItemEntity item);
	
	public void resignFromCourse(OperationEntity operation, ItemEntity item);
	
	public void updateGrade(OperationEntity operation, ItemEntity item);
	
	public void removeCourse(OperationEntity operation, ItemEntity item);
	
	public List<ItemBoundary> getRegisteredCourses(OperationEntity operation);
	
	public List<ItemBoundary> getAllCourses(OperationEntity operation);
}
