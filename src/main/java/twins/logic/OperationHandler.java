package twins.logic;

import java.util.List;
import twins.data.OperationEntity;
import twins.item.ItemBoundary;

public interface OperationHandler {
	public void registerToCourse(OperationEntity operation);
	
	public void resignFromCourse(OperationEntity operation);
	
	public void updateGrade(OperationEntity operation);
	
	public void removeCourse(OperationEntity operation);
	
	public List<ItemBoundary> getRegisteredCourses(OperationEntity operation);
	
	public List<ItemBoundary> getAllCourses(OperationEntity operation);
}
