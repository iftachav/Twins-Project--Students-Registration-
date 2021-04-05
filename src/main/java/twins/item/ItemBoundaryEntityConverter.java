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
		ib.setItemId(new ItemIdBoundary(itemEntity.getId(), itemEntity.getItemSpace()));
		
		if(itemEntity.getItemAttributes() != null)
			ib.setItemAttributes(itemEntity.getItemAttributes());
		else
			ib.setItemAttributes(null);
		
		return ib;
	}

	@Override
	public ItemEntity toEntity(ItemBoundary itemBoundary) {
		ItemEntity ie = new ItemEntity();
		
		if(itemBoundary.getActive() != null)
			ie.setActive(itemBoundary.getActive());
		else
			ie.setActive(false);
		
		if(itemBoundary.getItemId() != null) {
			ie.setId(itemBoundary.getItemId().getId());
			ie.setItemSpace(itemBoundary.getItemId().getSpace());
		}
			
		if(itemBoundary.getItemAttributes() != null)
			ie.setItemAttributes(itemBoundary.getItemAttributes());
		else
			ie.setItemAttributes(null);
		
		if(itemBoundary.getLocation() != null) {
			ie.setLat(itemBoundary.getLocation().getLat());
			ie.setLng(itemBoundary.getLocation().getLng());
		}
		
		if(itemBoundary.getCreatedBy() != null) {
			ie.setUserEmail(itemBoundary.getCreatedBy().getUserId().getEmail());
			ie.setUserSpace(itemBoundary.getCreatedBy().getUserId().getSpace());
		}

		ie.setName(itemBoundary.getName());
		ie.setType(itemBoundary.getType());
		
		return ie;
	}
	
}
