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
		System.err.println(space);
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
		
		String id = UUID.randomUUID().toString() + space;
		
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

		ItemEntity entity = null;
		
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
		
		if(entity == null)
			throw new RuntimeException("The requested item doesn't exist");
		
		return itemEntityConverter.toBoundary(entity);
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		if(userSpace == null || userEmail == null) 
			throw new RuntimeException("userSpace or userEmail can not be null");
		
		//filter only items matching userSpace && userEmail, convert them to ItemBoundary and export them to a Collection
		return items.values().stream().filter(e-> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
			.map(itemEntityConverter::toBoundary)
			.collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		if(itemId == null) 
			throw new RuntimeException("itemId can not be null");
		
		ItemEntity ie = null;
			//Search for the desired item
			for(Map.Entry<String, ItemEntity> entry : items.entrySet()) {
				if(Long.toString(entry.getValue().getId()).equals(itemId) && entry.getValue().getUserSpace().equals(userSpace) && 
						entry.getValue().getUserEmail().equals(userEmail) && entry.getValue().getItemSpace().equals(itemSpace)) {
					ie = entry.getValue();
				}
			}
		
		if(ie == null)
			throw new RuntimeException("The requested item doesn't exist");
		return itemEntityConverter.toBoundary(ie);
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		items.clear();
	}
	
}
