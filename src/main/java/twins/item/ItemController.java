package twins.item;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twins.logic.UpdatedItemService;

@RestController
public class ItemController {
	private UpdatedItemService itemService;
	
	@Autowired
	public ItemController(UpdatedItemService itemService) {
		this.itemService = itemService;
	}
	
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem(@RequestBody ItemBoundary input, @PathVariable("userSpace") String userSpace, 
			@PathVariable("userEmail") String userEmail) {
		return itemService.createItem(userSpace, userEmail, input);
	}
		
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateData (@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace, @PathVariable("itemId") String itemId, @RequestBody ItemBoundary update) {
		itemService.updateItem(userSpace, userEmail, itemSpace, itemId, update);
	}
	
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary getSpecificItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail, @PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String id) {
		return itemService.getSpecificItem(userSpace, userEmail, itemSpace, id);
	}

	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary[] getAllItems(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) {
		List<ItemBoundary> allItems=this.itemService.getAllItems(userSpace, userEmail,size,page);
		return allItems.toArray(new ItemBoundary[0]);
	}

	@RequestMapping(path = "/twins/admin/items/{userSpace}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteAllItems(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		itemService.deleteAllItems(userSpace, userEmail);
	}
}
