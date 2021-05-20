package twins.logic;

import java.util.List;

import twins.item.ItemBoundary;
import twins.item.ItemIdBoundary;

public interface UpdatedItemService extends ItemsService{
	
	public void addChildToItem(String parentId, ItemIdBoundary childIdBoundary ,String userEmail, String userSpace);

	public List<ItemBoundary> getAllChildren(String parentId,String userEmail, String userSpace);
	
	public List<ItemBoundary> getAllParents(String childId,String userEmail, String userSpace);
	
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail,int size, int page);

}
