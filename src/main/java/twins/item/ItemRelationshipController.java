package twins.item;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import twins.logic.UpdatedItemService;

public class ItemRelationshipController{
	private UpdatedItemService itemLogic;
	
	@Autowired
	public ItemRelationshipController(UpdatedItemService itemLogic) {
		super();
		this.itemLogic = itemLogic;
	}
	
	// add operation for creation relationships between message
	@RequestMapping(method = RequestMethod.PUT,
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addChildToItem (
			@PathVariable("itemId") String itemId, 
			@RequestBody ItemIdBoundary childIdBoundary) {
		this.itemLogic
			.addChildToItem(itemId, childIdBoundary.getId());
	}

	// operation for getting responses of a specific message
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getChildren (
			@PathVariable("itemId") String itemId){
		return this.itemLogic
			.getAllChildren(itemId)
			.toArray(new ItemBoundary[0]);
	}
	
	// operation for getting original message of another message
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parrents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getParents (@PathVariable("responseId") String responseId) {
		Optional<ItemBoundary> original = this.itemLogic.getParent(responseId);
		
		if (original.isPresent()) {
			return new ItemBoundary[] {original.get()};
		}else {
			return new ItemBoundary[0];
		}
	}
}
