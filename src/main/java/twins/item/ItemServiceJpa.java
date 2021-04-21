package twins.item;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.dal.ItemDao;
import twins.data.ItemEntity;
import twins.errors.BadRequestException;
import twins.errors.NotFoundException;
import twins.logic.ItemConverter;
import twins.logic.UpdatedItemService;

@Service
public class ItemServiceJpa implements UpdatedItemService{
	private ItemDao itemDao;
	private ItemConverter itemEntityConverter;
	private String space;
	
	public ItemServiceJpa() {
		
	}

	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
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
	@Transactional//(readOnly = false)
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		String newId;
		if(item!=null && item.getItemId()!=null && item.getItemId().getSpace()!=null && item.getItemId().getId()!=null)
			newId = item.getItemId().getSpace() + "_" + item.getItemId().getId(); //UUID.randomUUID().toString();
		else
			throw new BadRequestException();
		
		ItemEntity entity = this.itemEntityConverter.toEntity(item);
		
		entity.setUserSpace(userSpace);
		entity.setUserEmail(userEmail);
		entity.setId(newId);
		entity.setTimestamp(new Date());
		
		entity = this.itemDao.save(entity);
		
		return this.itemEntityConverter.toBoundary(entity);
	}
	
	@Override
	@Transactional
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		if(update==null)
			throw new BadRequestException();
		
		String id = itemSpace + "_" + itemId;
		Optional<ItemEntity> entityOptional =  itemDao.findById(id);
		
		if(!entityOptional.isPresent())
			throw new NotFoundException("Item id " + id + " doesn't exist");
		
		ItemEntity entity = entityOptional.get();
		
	
		boolean updated = false;
		if(update.getActive()!=null) {
			entity.setActive(update.getActive());
			updated=true;
		}
		if(update.getType() != null) {
			entity.setType(update.getType());
			updated = true;
		}
		if(update.getName() != null) {
			entity.setName(update.getName());
			updated = true;
		}
		if(update.getActive()!=null) {
			entity.setActive(update.getActive());
			updated=true;
		}
		if(update.getLocation() != null) {
			entity.setLat(update.getLocation().getLat());
			entity.setLng(update.getLocation().getLng());
			updated = true;
		}
		
		if(update.getItemAttributes() != null) {
			entity.setItemAttributes(this.itemEntityConverter.fromMapToJson(update.getItemAttributes()));
			updated = true;
		}
		
		if(updated)
			this.itemDao.save(entity);
		
		return itemEntityConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		Iterable<ItemEntity>  allEntities = this.itemDao.findAll();

		return StreamSupport
				.stream(allEntities.spliterator(), false) // get stream from iterable
				.map(this.itemEntityConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {	
		String id = itemSpace + "_" + itemId;
		Optional<ItemEntity> optionalItem = this.itemDao.findById(id);

		if(!optionalItem.isPresent())
			throw new NotFoundException("Item id " + id + " doesn't exist");
		
		ItemBoundary boundary = itemEntityConverter.toBoundary(optionalItem.get());
		
		return boundary;
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		this.itemDao.deleteAll();
	}

	@Override
	@Transactional
	public void addChildToItem(String parentId, String childId) {
		ItemEntity parent = this.itemDao
				.findById(parentId)
				.orElseThrow(()->new NotFoundException("could not find original parrent by id: " + parentId));
		
		ItemEntity child = this.itemDao
				.findById(childId)
				.orElseThrow(()->new NotFoundException("could not find original parrent by id: " + childId));
		
		parent.addChild(child);
		
		this.itemDao.save(parent);
		this.itemDao.save(child);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllChildren(String parentId) {
		ItemEntity parent = this.itemDao
				.findById(parentId)
				.orElseThrow(()->new NotFoundException("could not find original message by id: " + parentId));

		return parent
			.getChildren() // Set<MessageEntity>
			.stream() // Stream<MessageEntity>
			.map(this.itemEntityConverter::toBoundary)// Stream<MessageBoundary>
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ItemBoundary> getParent(String childId) {
		ItemEntity child = this.itemDao
				.findById(childId)
				.orElseThrow(()->new NotFoundException("could not find response message by id: " + childId));
		
		if (child.getParent() != null) {
			return Optional.of(
				this.itemEntityConverter
					.toBoundary(child.getParent()));
		
		}else {
			return Optional.empty();
		}
	}
	
}
