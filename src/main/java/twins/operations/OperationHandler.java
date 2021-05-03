package twins.operations;

import twins.data.ItemEntity;
import twins.data.UserEntity;

public interface OperationHandler {
	public void handle(String operationType, UserEntity user, ItemEntity item);
}
