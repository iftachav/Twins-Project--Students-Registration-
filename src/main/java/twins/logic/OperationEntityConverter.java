package twins.logic;

import twins.data.OperationEntity;
import twins.operations.OperationBoundary;

public interface OperationEntityConverter {

		public OperationBoundary toBoundary(OperationEntity entity);

		public OperationEntity fromBoundary(OperationBoundary boundary);


}
