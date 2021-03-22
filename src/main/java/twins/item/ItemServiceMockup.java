package twins.item;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twins.data.ItemEntity;
import twins.logic.ItemConverter;
import twins.logic.ItemService;

@Service
public class ItemServiceMockup implements ItemService{
	private Map<String, ItemEntity> items;
	private ItemConverter itemEntityConverter;
	private final String ID_STRING = "iftach.avraham";
	
	public ItemServiceMockup() {
		this.items = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setItemEntityConverter(ItemConverter itemEntityConverter) {
		this.itemEntityConverter = itemEntityConverter;
	}
	
	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		String id = UUID.randomUUID().toString() + ID_STRING;
		
		ItemEntity entity = itemEntityConverter.toEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		items.put(id, entity);
		
		return itemEntityConverter.toBoundary(entity);		
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		if(itemId == null) 
			throw new RuntimeException("itemId can not be null");
		
			//Creating the updated entity
			ItemEntity entity = itemEntityConverter.toEntity(update);		
			entity.setUserSpace(userSpace);
			entity.setUserEmail(userEmail);
			entity.setItemSpace(itemSpace);
			
			//Search for the desired item
			for(Map.Entry<String, ItemEntity> entry : items.entrySet()) {
				if(Long.toString(entry.getValue().getId()) == itemId && entry.getValue().getUserSpace() == userSpace && 
						entry.getValue().getUserEmail() == userEmail && entry.getValue().getItemSpace() == itemSpace) {
					entry.setValue(entity);
					break;
				}
			}
			
			return itemEntityConverter.toBoundary(entity);
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//filter only items matching userSpace && userEmail, convert them to ItemBoundary and export them to a Collection
		if(userSpace == null || userEmail == null) 
			throw new RuntimeException("userSpace or userEmail can not be null");
		
		return items.values().stream().filter(e-> e.getUserSpace() == userSpace && e.getUserEmail() == userEmail)
			.map(itemEntityConverter::toBoundary)
			.collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		if(itemId != null) {
			//Search for the desired item
			for(Map.Entry<String, ItemEntity> entry : items.entrySet()) {
				if(Long.toString(entry.getValue().getId()) == itemId && entry.getValue().getUserSpace() == userSpace && 
						entry.getValue().getUserEmail() == userEmail && entry.getValue().getItemSpace() == itemSpace) {
					return itemEntityConverter.toBoundary(entry.getValue());
				}
			}
		}
		
		throw new RuntimeException("itemId can not be null");
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		items.clear();
	}
	
}
