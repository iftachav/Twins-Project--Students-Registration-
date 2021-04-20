package twins.operations;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.OperationEntity;
import twins.item.ItemIdBoundary;
import twins.logic.OperationEntityConverter;
import twins.user.UserId;

@Component
public class OperationEntityConverterImplementation implements OperationEntityConverter {

	private ObjectMapper jackson;

	public OperationEntityConverterImplementation() {
		this.jackson = new ObjectMapper();
	}
	
	@Override
	public OperationBoundary toBoundary(OperationEntity entity) {
		OperationBoundary rv = new OperationBoundary();
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setInvokedBy(new UserIdWrapper());
		rv.getInvokedBy().setUserId(new UserId(entity.getUserSpace(), entity.getUserEmail()));
		rv.setItem(new ItemWrapper());
		rv.getItem().setItemId(new ItemIdBoundary(entity.getItemId(), entity.getItemSpace()));
		rv.setOperationAttributes(this.fromJsonToMap(entity.getOperationAttributes()));
		rv.setOperationId(new OperationId(entity.getOperationSpace(), entity.getOperationId()));
		rv.setType(entity.getType());
		return rv;
	}
	

	@Override
	public OperationEntity fromBoundary(OperationBoundary boundary) {
		OperationEntity rv= new OperationEntity();
		if(boundary.getOperationId()!= null) {
			rv.setOperationId(boundary.getOperationId().getId());
			rv.setOperationSpace(boundary.getOperationId().getSpace());
		}
		if(boundary.getType()!= null) {
			rv.setType(boundary.getType());
		}
		if(boundary.getItem()!= null) {
			rv.setItemId(boundary.getItem().getItemId().getId());
			rv.setItemSpace(boundary.getItem().getItemId().getSpace());
		}
		rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
		if(boundary.getInvokedBy()!= null) {
			rv.setUserEmail(boundary.getInvokedBy().getUserId().getEmail());
			rv.setUserSpace(boundary.getInvokedBy().getUserId().getSpace());
		}
		rv.setOperationAttributes(this.fromMapToJson(boundary.getOperationAttributes()));
		return rv;
	}

	
	@Override
	public String fromMapToJson (Map<String, Object> value) { // marshalling: Java->JSON
		
		try {
			return this.jackson
				.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}
	
	@Override
	public Map<String, Object> fromJsonToMap (String json){ // unmarshalling: JSON->Java
		try {
			return this.jackson
				.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


}
