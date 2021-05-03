package twins.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twins.data.ItemEntity;
import twins.data.UserEntity;
import twins.errors.BadRequestException;
import twins.logic.ItemsService;

@Service
public class Handler implements OperationHandler{
	
	private ItemsService itemService;
	
	@Autowired
	public void setItemsService(ItemsService itemService) {
		this.itemService = itemService;
	}
	
	public Handler() { }

	@Override
	public void handle(String operationType, UserEntity user, ItemEntity item) {
		if(operationType.equals(OperationTypes.registerToCourse.toString())) {
			//TODO check for hours collision
		} else if(operationType.equals(OperationTypes.resignFromCourse.toString())) {
			
		} else if(operationType.equals(OperationTypes.updateGrade.toString())) {
			
		} else if(operationType.equals(OperationTypes.removeCourse.toString())) {
			
		} else {
			throw new BadRequestException("Operation " + operationType + " isn't valid");
		}
	}
}
