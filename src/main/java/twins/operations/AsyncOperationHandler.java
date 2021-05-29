package twins.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import twins.dal.OperationDao;
import twins.data.OperationEntity;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationHandler;


@Component
public class AsyncOperationHandler {
	private ObjectMapper jackson;
	private OperationHandler operationHandler;
	private OperationDao operationDao;
	private OperationEntityConverter operationEntityConverter;
	
	public AsyncOperationHandler() {
		this.jackson = new ObjectMapper();
	}
	
	@Autowired
	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
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

			OperationEntity entity = operationEntityConverter.fromBoundary(operation);
			
			if(operation.getType().equals(OperationTypes.registerToCourse.toString()))
				this.operationHandler.registerToCourse(entity);
			else if(operation.getType().equals(OperationTypes.resignFromCourse.toString()))
				this.operationHandler.resignFromCourse(entity);
			else if(operation.getType().equals(OperationTypes.updateGrade.toString()))
				this.operationHandler.updateGrade(entity);
			else if(operation.getType().equals(OperationTypes.removeCourse.toString()))
				this.operationHandler.removeCourse(entity);				
			
			operationDao.save(entity); //moved from OperationServiceJpa::invokeAsynchronousOperation
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
