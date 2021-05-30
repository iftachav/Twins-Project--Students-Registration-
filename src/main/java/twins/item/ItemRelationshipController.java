package twins.item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.UpdatedItemService;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ItemRelationshipController{
	private UpdatedItemService itemLogic;
	
	@Autowired
	public ItemRelationshipController(UpdatedItemService itemLogic) {
		super();
		this.itemLogic = itemLogic;
	}
	
	@RequestMapping(method = RequestMethod.PUT,
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addChildToItem (
			@PathVariable("itemId") String itemId, 
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("userSpace") String userSpace,
			@RequestBody ItemIdBoundary childIdBoundary) {
		this.itemLogic
			.addChildToItem(itemId, childIdBoundary,userEmail,userSpace);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getChildren (
			@PathVariable("itemId") String itemId,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("itemSpace") String itemSpace){
		return this.itemLogic
			.getAllChildren(itemId,userEmail,userSpace)
			.toArray(new ItemBoundary[0]);
	}
	
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getParents (
			@PathVariable("itemId") String itemId,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("itemSpace") String itemSpace) {
		return this.itemLogic.getAllParents(itemId,userEmail,userSpace).toArray(new ItemBoundary[0]);

	}
}
