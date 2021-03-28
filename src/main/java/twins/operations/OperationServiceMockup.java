package twins.operations;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.data.OperationEntity;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationService;

@Service
public class OperationServiceMockup implements OperationService{
	private Map<String, OperationEntity> operations;
	private OperationEntityConverter operationEntityConverter;
	private long id;
	
	public OperationServiceMockup() {
		//this is a thread safe collection
		this.operations=Collections.synchronizedMap(new HashMap<>());
		this.id = 0;
	}
	
	@Autowired
	public void setEntityConverter(OperationEntityConverter entityConverter) {
		this.operationEntityConverter = entityConverter;
	}

	@Override
	public Object invokeOperation(OperationBoundary operation) {
		if(operation==null)
			return null;
		operation.getOperationId().setId(""+this.id++); // ?
		operation.setCreatedTimestamp(new Date());
		operation.getInvokedBy().getUserId().setSpace("2021b.iftach.avraham");
		operation.getItem().getItemId().setSpace("2021b.iftach.avraham");
		operation.getOperationId().setSpace("2021b.iftach.avraham");
		OperationEntity op = operationEntityConverter.fromBoundary(operation);
		String newId= UUID.randomUUID().toString()+"_"+operation.getInvokedBy().getUserId().getEmail()+"_"+operation.getInvokedBy().getUserId().getSpace();
		operations.put(newId, op);
		return operation;
		//TODO need to use spring.application.name.@@@@@@@@@@@@@@@@@@@@@@@@@@@
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		if(operation==null)
			return null;
		operation.getOperationId().setId(""+this.id++); // ?
		operation.setCreatedTimestamp(new Date());
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
}
