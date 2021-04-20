package twins.logic;

import java.util.Map;

import twins.data.ItemEntity;
import twins.item.ItemBoundary;

public interface ItemConverter {
	public ItemBoundary toBoundary(ItemEntity itemEntity);
	
	public ItemEntity toEntity(ItemBoundary itemBoundary);
	
	public String fromMapToJson (Map<String, Object> value);
	
	public Map<String, Object> fromJsonToMap (String json);
}
