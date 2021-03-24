package twins.item;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twins.data.ItemEntity;
import twins.logic.ItemConverter;
import twins.logic.ItemsService;

@Service
public class ItemServiceMockup implements ItemsService{
	private Map<String, ItemEntity> items;
	private ItemConverter itemEntityConverter;
	
	//TODO need to use spring.application.name.
	//@Value("${spring.application.name:2021b.iftach.avraham}")
	private final String space = "2021b.iftach.avraham";
	
	public ItemServiceMockup() {
		this.items = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setItemEntityConverter(ItemConverter itemEntityConverter) {
		this.itemEntityConverter = itemEntityConverter;
	}
	
	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		if(userSpace == null || userEmail == null) 
			throw new RuntimeException("userSpace or userEmail can not be null");
		if(item.getCreatedTimestamp() == null)
			item.setTimestamp(new Date());
		
		String id = this.space + "_";
		if(item.getItemId().getId() == null)
			id += UUID.randomUUID().toString();
		else
			id += item.getItemId().getId().toString();
		
		
		ItemEntity entity = itemEntityConverter.toEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		items.put(id, entity);
		
		return itemEntityConverter.toBoundary(entity);		
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		if(itemId == null || itemSpace == null) 
			throw new RuntimeException("itemId or itemSpace can not be null");
		
		String id = itemSpace + "_" + itemId;
		ItemEntity entity =  items.get(id);
		
		//TODO old implementation. Have to look into the key identifier and it's components
/*		ItemEntity entity = null;
		
		//Search for the desired item
		for(Map.Entry<String, ItemEntity> entry : items.entrySet()) {
			if(Long.toString(entry.getValue().getId()).equals(itemId) && entry.getValue().getUserSpace().equals(userSpace) && 
					entry.getValue().getUserEmail().equals(userEmail) && entry.getValue().getItemSpace().equals(itemSpace)) {
				
				//Creating the updated entity
				entity =  itemEntityConverter.toEntity(update);	
				
				//Keep these values unchanged
				entity.setUserSpace(entry.getValue().getUserSpace());
				entity.setUserEmail(entry.getValue().getUserEmail());
				entity.setItemSpace(entry.getValue().getItemSpace());
				entity.setTimestamp(entry.getValue().getTimestamp());
				
				//updating the DB
				entry.setValue(entity);
				break;
			}
		}
*/		
		if(entity == null)
			throw new RuntimeException("Item id " + id + " doesn't exist");
		
		ItemEntity updateEntity = itemEntityConverter.toEntity(update);	
		
		//Keep these values unchanged
		updateEntity.setUserSpace(entity.getUserSpace());
		updateEntity.setUserEmail(entity.getUserEmail());
		updateEntity.setItemSpace(entity.getItemSpace());
		updateEntity.setTimestamp(entity.getTimestamp());
		
		items.put(id, updateEntity);
		
		return itemEntityConverter.toBoundary(updateEntity);
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		if(userSpace == null || userEmail == null) 
			throw new RuntimeException("userSpace or userEmail can not be null");

		//filter only items matching userSpace && userEmail, convert them to ItemBoundary and export them to a Collection
//		return items.values().stream().filter(e-> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
//			.map(itemEntityConverter::toBoundary)
//			.collect(Collectors.toList());
		
		//convert all items ItemBoundary and export them to a Collection
		return items.values().stream().map(itemEntityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		if(itemId == null || itemSpace == null) 
			throw new RuntimeException("itemId or itemSpace can not be null");
		
		
/*		TODO old implementation. Have to look into the key identifier and it's components
 * 		ItemEntity ie = null;
			//Search for the desired item
			for(Map.Entry<String, ItemEntity> entry : items.entrySet()) {
				if(Long.toString(entry.getValue().getId()).equals(itemId) && entry.getValue().getUserSpace().equals(userSpace) && 
						entry.getValue().getUserEmail().equals(userEmail) && entry.getValue().getItemSpace().equals(itemSpace)) {
					ie = entry.getValue();
				}
			}
*/		
		String id = itemSpace + "_" + itemId;
		ItemEntity ie = items.get(id);
		if(ie == null)
			throw new RuntimeException("Item id " + id + " doesn't exist");
		return itemEntityConverter.toBoundary(ie);
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		items.clear();
	}
	
}
