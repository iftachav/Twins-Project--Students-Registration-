package twins.operations;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.dal.OperationDao;
import twins.data.OperationEntity;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationService;


@Service
public class OperationServiceJpa implements OperationService{
	private OperationDao operationDao;
	private OperationEntityConverter operationEntityConverter;
	private String springApplicationName;
	
	public OperationServiceJpa() {
	}
	
	@Autowired
	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}
	
	@Autowired
	public void setEntityConverter(OperationEntityConverter entityConverter) {
		this.operationEntityConverter = entityConverter;
	}

	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}
	
	@Override
	@Transactional
	public Object invokeOperation(OperationBoundary operation) {
		if(operation == null)
			throw new RuntimeException("Null Operation Received.");
		setSpringApplicationName(springApplicationName);
		operation.setCreatedTimestamp(new Date());
		
		if(operation.getType() == null)
			throw new RuntimeException("Null operation type passed");
		
		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("Null Invoked By Element Received.");
		operation.getInvokedBy().getUserId().setSpace(springApplicationName);
		if(!checkEmail(operation.getInvokedBy().getUserId().getEmail()))
			throw new RuntimeException("Email Is Not Valid.");
		
		if(operation.getItem() == null || operation.getItem().getItemId() == null || operation.getItem().getItemId().getId() == null)
			throw new RuntimeException("Null Item Element Received.");
		operation.getItem().getItemId().setSpace(springApplicationName);
		String newId= UUID.randomUUID().toString()+"_"+operation.getInvokedBy().getUserId().getEmail()+"_"+operation.getInvokedBy().getUserId().getSpace();
		operation.getOperationId().setId(newId);
		operation.getOperationId().setSpace(springApplicationName);
		
		OperationEntity op = operationEntityConverter.fromBoundary(operation);
		operationDao.save(op);
		return operation;
	}

	@Override
	@Transactional
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		if(operation == null)
			throw new RuntimeException("Null Operation Received.");
		setSpringApplicationName(springApplicationName);
		operation.setCreatedTimestamp(new Date());
		
		if(operation.getType() == null)
			throw new RuntimeException("Null operation type passed");
		
		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("Null Invoked By Element Received.");
		operation.getInvokedBy().getUserId().setSpace(springApplicationName);
		if(!checkEmail(operation.getInvokedBy().getUserId().getEmail()))
			throw new RuntimeException("Email Is Not Valid.");
		
		if(operation.getItem() == null || operation.getItem().getItemId() == null || operation.getItem().getItemId().getId() == null)
			throw new RuntimeException("Null Item Element Received.");
		operation.getItem().getItemId().setSpace(springApplicationName);
		

		String newId= UUID.randomUUID().toString()+"_"+operation.getInvokedBy().getUserId().getEmail()+"_"+operation.getInvokedBy().getUserId().getSpace();
		operation.getOperationId().setId(newId);
		operation.getOperationId().setSpace(springApplicationName);
		OperationEntity op = operationEntityConverter.fromBoundary(operation);
		operationDao.save(op);
		return operation;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		Iterable<OperationEntity> allEntities=this.operationDao.findAll();
		return StreamSupport.stream(allEntities.spliterator(), false).map(this.operationEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		this.operationDao.deleteAll();
	}
	
	public boolean checkEmail(String email) {
		if(email.equals(null))
			return false;
		String[] splitted=email.split("@");
		int size1=splitted.length;
		if(size1>2 || size1<1)
			return false;
		int size2=splitted[1].split("//.").length;
		if(size2>3 || size2<1)
			return false;
		return true;
	}
	
}
