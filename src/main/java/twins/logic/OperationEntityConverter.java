package twins.logic;

import java.util.Map;

import twins.data.OperationEntity;
import twins.operations.OperationBoundary;

public interface OperationEntityConverter {

		public OperationBoundary toBoundary(OperationEntity entity);

		public OperationEntity fromBoundary(OperationBoundary boundary);

		public Map<String, Object> fromJsonToMap(String json);

		public String fromMapToJson(Map<String, Object> value);


}
