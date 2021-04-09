package twins.operations;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import twins.data.OperationEntity;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationService;

@Service
public class OperationServiceMockup implements OperationService{
	private Map<String, OperationEntity> operations;
	private OperationEntityConverter operationEntityConverter;
	private long id;
	private String springApplicationName;
	
	public OperationServiceMockup() {
		//this is a thread safe collection
		this.operations=Collections.synchronizedMap(new HashMap<>());
		this.id = 0;
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
		
		if(operation.getOperationId() == null)
			throw new RuntimeException("Null Operation Id Received.");
		operation.getOperationId().setId(""+this.id++); // ?
		operation.getOperationId().setSpace(springApplicationName);
		
		OperationEntity op = operationEntityConverter.fromBoundary(operation);
		String newId= UUID.randomUUID().toString()+"_"+operation.getInvokedBy().getUserId().getEmail()+"_"+operation.getInvokedBy().getUserId().getSpace();
		operations.put(newId, op);
		return operation.getOperationId().getId();
	}

	@Override
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
		
		if(operation.getOperationId() == null)
			throw new RuntimeException("Null Operation Id Received.");
		operation.getOperationId().setId(""+this.id++); // ?
		operation.getOperationId().setSpace(springApplicationName);
		
		OperationEntity op = operationEntityConverter.fromBoundary(operation);
		String newId= UUID.randomUUID().toString()+"_"+operation.getInvokedBy().getUserId().getEmail()+"_"+operation.getInvokedBy().getUserId().getSpace();
		operations.put(newId, op);
		return operation;
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		return this.operations.values().stream().map(this.operationEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		operations.clear();
		this.id = 0;
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
