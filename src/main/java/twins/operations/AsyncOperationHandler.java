package twins.operations;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import twins.dal.ItemDao;
import twins.dal.UserDao;
import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationHandler;


@Component
public class AsyncOperationHandler {
	private ObjectMapper jackson;
	private OperationHandler operationHandler;
	private UserDao userDao;
	private ItemDao itemDao;
	private OperationEntityConverter operationEntityConverter;
	
	public AsyncOperationHandler() {
		this.jackson = new ObjectMapper();
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	@Autowired
	public void setOperationHandler(OperationHandler operationHandler) {
		this.operationHandler = operationHandler;
	}
	
	@Autowired
	public void setEntityConverter(OperationEntityConverter entityConverter) {
		this.operationEntityConverter = entityConverter;
	}
	
	@Transactional
	@JmsListener(destination = "operationInbox") 
	public void handleJson (String json) {
		try {

			OperationBoundary operation = this.jackson
				.readValue(json, OperationBoundary.class);
			
			Optional<UserEntity> optionalUser = this.userDao.findById(operation.getInvokedBy().getUserId().getEmail() + 
					"@@" + operation.getInvokedBy().getUserId().getSpace());	//search user by: UserEmail@@Space
	
			Optional<ItemEntity> optionalItem = this.itemDao.findById(operation.getItem().getItemId().getId());	//search item by: Space_ItemId
			ItemEntity item = optionalItem.get();
			OperationEntity entity = operationEntityConverter.fromBoundary(operation);
			
			if(operation.getType().equals(OperationTypes.registerToCourse.toString()))
				this.operationHandler.registerToCourse(entity);
			else if(operation.getType().equals(OperationTypes.resignFromCourse.toString()))
				this.operationHandler.resignFromCourse(entity);
			else if(operation.getType().equals(OperationTypes.updateGrade.toString()))
				this.operationHandler.updateGrade(entity);
			else if(operation.getType().equals(OperationTypes.removeCourse.toString()))
				this.operationHandler.removeCourse(entity);				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
