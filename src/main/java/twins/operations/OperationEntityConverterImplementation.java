package twins.operations;

import org.springframework.stereotype.Component;

import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.item.ItemIdBoundary;
import twins.logic.OperationEntityConverter;
import twins.user.UserBoundary;
import twins.user.UserId;

@Component
public class OperationEntityConverterImplementation implements OperationEntityConverter {

	@Override
	public OperationBoundary toBoundary(OperationEntity entity) {
		OperationBoundary rv = new OperationBoundary();
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setInvokedBy(new UserIdWrapper());
		rv.getInvokedBy().setUserId(new UserId(entity.getUserSpace(), entity.getUserEmail()));
		rv.setItem(new ItemWrapper());
		rv.getItem().setItemId(new ItemIdBoundary(entity.getItemId(), entity.getItemSpace()));
		rv.setOperationAttributes(entity.getOperationAttributes());
		rv.setOperationId(new OperationId(entity.getOperationSpace(), entity.getOperationId()));
		rv.setType(entity.getType());
		return rv;
	}

	@Override
	public OperationEntity fromBoundary(OperationBoundary boundary) {
		OperationEntity rv = new OperationEntity(boundary.getOperationId().getSpace(),
				boundary.getOperationId().getId(), boundary.getType(), boundary.getItem().getItemId().getSpace(),
				boundary.getItem().getItemId().getId(), boundary.getCreatedTimestamp(),
				boundary.getInvokedBy().getUserId().getSpace(), boundary.getInvokedBy().getUserId().getEmail(),
				boundary.getOperationAttributes());
		return rv;
	}

}
