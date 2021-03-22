package twins.item;

import org.springframework.stereotype.Component;

import twins.data.ItemEntity;
import twins.logic.ItemConverter;
import twins.user.UserId;

@Component
public class ItemBoundaryEntityConverter implements ItemConverter{

	@Override
	public ItemBoundary toBoundary(ItemEntity itemEntity) {
		ItemBoundary ib = new ItemBoundary(itemEntity.getType(), itemEntity.getName(), itemEntity.isActive(), 
				new LocationBoundary(itemEntity.getLat(), itemEntity.getLng()));
		ib.setCreatedBy(new CreatedByBoundary(new UserId(itemEntity.getItemSpace(), itemEntity.getUserEmail())));
		ib.setTimestamp(itemEntity.getTimestamp());
		ib.setItemId(new ItemIdBoundary(itemEntity.getId()));
		
		if(itemEntity.getItemAttributes() != null)
			ib.setItemAttributes(itemEntity.getItemAttributes());
		else
			ib.setItemAttributes(null);
		
		return ib;
	}

	@Override
	public ItemEntity toEntity(ItemBoundary itemBoundary) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
