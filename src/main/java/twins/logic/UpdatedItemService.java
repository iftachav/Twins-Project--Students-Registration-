package twins.item;

import java.util.List;
import java.util.Optional;

import twins.logic.ItemsService;

public interface UpdatedItemService extends ItemsService{
	
	public void addChildToItem(String parentId, String childId);

	public List<ItemBoundary> getAllChildren(String parentId);

	public Optional<ItemBoundary> getParent(String childId);

}
