package twins.operations;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import twins.data.ItemEntity;
import twins.data.UserEntity;
import twins.logic.UserItemConverter;

@Component
public class UserToItemConverter implements UserItemConverter{
	private String space;
	
	public UserToItemConverter() { }
	
	public String getSpace() {
		return space;
	}

	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpace(String space) {
		this.space = space;
	}


	/*
	 * TODO
	 * Flawed implementation. Student mail and space saved in CreatedBy section and not in itemAttributes
	 */
	@Override
	public ItemEntity UserToItem(UserEntity user) {	
		ItemEntity item = new ItemEntity();
		
		String newId = this.space + "_" + UUID.randomUUID().toString();
		item.setItemSpace(this.space);
		item.setId(newId);
		item.setType("Student"); 	//consider replace it with an Enum
		item.setName(user.getEmail());
		item.setActive(true);
		item.setTimestamp(new Date());
		item.setUserEmail(user.getEmail());
		item.setUserSpace(user.getSpace());
		item.setLat(0);
		item.setLng(0);
		
		return item;
	}

}
