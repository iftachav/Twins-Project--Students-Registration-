package twins.logic;

import java.util.List;

import twins.operations.OperationBoundary;

public interface UpdatedOperationService extends OperationService {

	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail,int size, int page);

}
