package twins.logic;

import twins.data.ItemEntity;
import twins.item.ItemBoundary;

public interface ItemConverter {
	public ItemBoundary toBoundary(ItemEntity itemEntity);
	
	public ItemEntity toEntity(ItemBoundary itemBoundary);
}
