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
//import org.springframework.stereotype.Service;
import twins.data.ItemEntity;
import twins.logic.ItemConverter;
import twins.logic.ItemsService;

/*
 * TODO itemId and map key what the relations between them?
 */

//@Service
public class ItemServiceMockup implements ItemsService{
	private Map<String, ItemEntity> items;
	private ItemConverter itemEntityConverter;
	private String space;
	
	public ItemServiceMockup() {
		this.items = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Autowired
	public void setItemEntityConverter(ItemConverter itemEntityConverter) {
		this.itemEntityConverter = itemEntityConverter;
	}
	
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpace(String space) {
		this.space = space;
	}
	
	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {		
		ItemEntity entity = itemEntityConverter.toEntity(item);
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		entity.setTimestamp(new Date());
		
		String id = UUID.randomUUID().toString();
		entity.setId(id);
		entity.setItemSpace(this.space);
		items.put((this.space + "_" + id), entity);
		
		return itemEntityConverter.toBoundary(entity);		
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		String id = itemSpace + "_" + itemId;
		ItemEntity entity =  items.get(id);
		if(entity == null)
			throw new RuntimeException("Item id " + id + " doesn't exist");
		
		boolean updated = false;
		if(update.getType() != null) {
			entity.setType(update.getType());
			updated = true;
		}
		if(update.getName() != null) {
			entity.setName(update.getName());
			updated = true;
		}
		if(update.getActive() != null) {
			entity.setActive(update.getActive());
			updated = true;
		}
		if(update.getLocation() != null) {
			entity.setLat(update.getLocation().getLat());
			entity.setLng(update.getLocation().getLng());
			updated = true;
		}
//		if(update.getItemAttributes() != null) {
//			entity.setItemAttributes(update.getItemAttributes());
//			updated = true;
//		}
//		
		if(updated)
			this.items.put(id, entity);
		
		return itemEntityConverter.toBoundary(entity);
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//filter only items matching userSpace && userEmail, convert them to ItemBoundary and export them to a Collection
/*		return items.values().stream().filter(e-> e.getUserSpace().equals(userSpace) && e.getUserEmail().equals(userEmail))
			.map(itemEntityConverter::toBoundary)
			.collect(Collectors.toList());
*/
		//convert all items ItemBoundary and export them to a Collection
		return items.values().stream().map(itemEntityConverter::toBoundary).collect(Collectors.toList());
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {	
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
