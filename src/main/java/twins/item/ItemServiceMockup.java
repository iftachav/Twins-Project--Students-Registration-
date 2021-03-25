package twins.item;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twins.data.ItemEntity;
import twins.logic.ItemConverter;
import twins.logic.ItemsService;

/*
 * TODO need to use spring.application.name
 * TODO itemId and map key what the relations between them?
 */

@Service
public class ItemServiceMockup implements ItemsService{
	private Map<String, ItemEntity> items;
	private ItemConverter itemEntityConverter;
	private long id;
	
	//@Value("${spring.application.name:2021b.iftach.avraham}")
	private final String space = "2021b.iftach.avraham";
	
	public ItemServiceMockup() {
		this.items = Collections.synchronizedMap(new HashMap<>());
		this.id = 0;
	}
	
	@Autowired
	public void setItemEntityConverter(ItemConverter itemEntityConverter) {
		this.itemEntityConverter = itemEntityConverter;
	}

	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {		
/*		if(item.getItemId() == null || item.getItemId().getId() == null)
			item.setItemId(new ItemIdBoundary(Long.parseLong(UUID.randomUUID().toString())));
			
		id += item.getItemId().getId().toString();
*/	
		ItemEntity entity = itemEntityConverter.toEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		entity.setTimestamp(new Date());
		entity.setId(this.id++);
		entity.setItemSpace(this.space);
		items.put((this.space + "_" + Long.toString(entity.getId())), entity);
		
		return itemEntityConverter.toBoundary(entity);		
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		
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
		String id = itemSpace + "_" + itemId;
		ItemEntity entity =  items.get(id);
		if(entity == null)
			throw new RuntimeException("Item id " + id + " doesn't exist");
		
		ItemEntity updateEntity = itemEntityConverter.toEntity(update);	
		
		//Keep these values unchanged
		updateEntity.setUserSpace(entity.getUserSpace());
		updateEntity.setUserEmail(entity.getUserEmail());
		updateEntity.setItemSpace(entity.getItemSpace());
		updateEntity.setTimestamp(entity.getTimestamp());
		updateEntity.setId(entity.getId());
		
		items.put(id, updateEntity);
		
		return itemEntityConverter.toBoundary(updateEntity);
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//filter only items matching userSpace && userEmail, convert them to ItemBoundary and export them to a Collection
//		return items.values().stream().filter(e-> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
//			.map(itemEntityConverter::toBoundary)
//			.collect(Collectors.toList());
		
		//convert all items ItemBoundary and export them to a Collection
		System.err.println(items.keySet().toString());
		return items.values().stream().map(itemEntityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
 /* 		ItemEntity ie = null;
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
