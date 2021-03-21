package twins.operations;

import twins.item.ItemIdBoundary;

public class ItemWrapper {
	private ItemIdBoundary itemId;
	
	public ItemWrapper() {
		this.itemId = new ItemIdBoundary();
	}

	public ItemIdBoundary getItemId() {
		return itemId;
	}

	public void setItemId(ItemIdBoundary itemId) {
		this.itemId = itemId;
	}
	
	
}
