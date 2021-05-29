package twins.logic;

import twins.data.ItemEntity;
import twins.data.UserEntity;

public interface UserItemConverter {
	
	public ItemEntity UserToItem(UserEntity user, String type);
}
