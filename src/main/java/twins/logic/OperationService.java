package twins.logic;

import java.util.List;

import twins.operations.OperationBoundary;

public interface OperationService {
	
	public Object invokeOperation(OperationBoundary operation);

	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation);

	@Deprecated
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail);

	public void deleteAllOperations(String adminSpace, String adminEmail);

}
