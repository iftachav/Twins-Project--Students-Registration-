package demo.item;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;






@RestController
public class ItemController {
	//Create New Item
//	localhost:8080/twins/items/iftach/ds@fsd
		@RequestMapping(
				path = "/twins/items/{userSpace}/{userEmail}",
				method = RequestMethod.POST,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary createNewItem(@RequestBody ItemBoundary input) {
			// STUB implementation - do nothing - focus on input and output only
			input.setItemId(new ItemIdBoundary(1L));
			return input;
		}
		
		
//		Update an item.		
//		localhost:8080/twins/items/iftach/ds@fsd/check/5	
		@RequestMapping(
				path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
				method = RequestMethod.PUT,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void updateData (
				@PathVariable("userSpace") String userSpace, 
				@PathVariable("userEmail") String userEmail,
				@PathVariable("itemSpace") String itemSpace,
				@PathVariable("itemId") Long itemId,
				@RequestBody ItemBoundary update) {
			// STUB implementation - do nothing
		}
		
		
		
		
		
		
// Retreive Specific Item
// localhost:8080/twins/items/iftach/ds@fsd/check/5
	@RequestMapping(
			//didnt finish, need to talk to arad about user details
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary retrieveSpecificItem (@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") Long id){
		// STUB implementation
		Map<String, Object> map= new HashMap<>();
		map.put("key1", true);
		ItemBoundary iB= new ItemBoundary();
		iB.setName("Meron");
		ItemIdBoundary iIB= new ItemIdBoundary(id);
		iIB.setSpace(itemSpace);
		iB.setItemId(iIB);
		iB.setItemAttributes(map);
		return iB;
	}
	
	
//	Get all items.
//	localhost:8080/twins/items/iftach/ds@fsd

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary[] hello () {
			//AtomicLong counter = new AtomicLong(1L);
			// STUB implementation - create 3 items as an array and return it
			return 
			  Stream
				.of(new ItemBoundary(),
					new ItemBoundary("type1", "Meron", true),
					new ItemBoundary("type2", "Iftach", false)) // create 3 boundaries
				// convert each boundary to other type / object
				.map(input->{
//					input.setName(new NameBoundary("Jane", "Smith"));
//					input.setCounter(counter.getAndIncrement());
					return input;
				})// lambda 
				.collect(Collectors.toList())// stream all boundaries to a collection;
				.toArray(new ItemBoundary[0]); // convert list to array
		}
	
	
	
	

	//Delete All Items
//	localhost:8080/twins/admin/items/fsda/vcx
	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
//				path = "/twins/admin",
			method = RequestMethod.DELETE)
	public void deleteAllItems () {
		// STUB implementation - do nothing
	}
}
