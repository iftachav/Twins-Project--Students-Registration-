package twins.logic;

import java.util.List;

import twins.item.ItemBoundary;
import twins.item.ItemIdBoundary;

public interface UpdatedItemService extends ItemsService{
	
	public void addChildToItem(String parentId, ItemIdBoundary childIdBoundary);

	public List<ItemBoundary> getAllChildren(String parentId);
	
	public List<ItemBoundary> getAllParents(String childId);

}
