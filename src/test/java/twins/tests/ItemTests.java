package twins.tests;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import twins.item.ItemBoundary;
import twins.item.LocationBoundary;

/*
 * TODO:
 * 1. create an item with null/invalid values
 * 2. update item with invalid values/null values
 * 3. update space/email/timeStamp of an existing item 
 * 4. get child/parent of an item
 * 5. check the map of an existing item 
 * How can we test there's nothing on the server after a DELETE?
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemTests {
	private int port;
	private String space;
	private String baseUrl;
	private RestTemplate restTemplate;
	
	//dummy items variables
	private String userEmail;
	private String itemName;
	private String itemType;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void initTest() {
		this.restTemplate = new RestTemplate();
		this.baseUrl = "http://localhost:" + this.port + "/twins";
		this.space = "2021b.iftach.avraham";
		
		this.userEmail = "a@demo.com";
		this.itemName  = "demoItem";
		this.itemType  = "demoType";
	}
	
	@AfterEach
	public void tearDown() {
		this.restTemplate.delete(this.baseUrl + "/admin/items/{userSpace}/{userEmail}", this.space, this.userEmail);
	}
	
	@Test
	public void testContext() throws Exception { }
	
	@Test
	public void testCreateItem() throws Exception {
		/*	Given: the server is up
		 * 	When: we POST item without itemId to /items/{userSpace}/{userEmail}
		 * 	Then: the server creates an Item with some default values and store it in the DB 
		 * 	And: returns an ItemBoundary with initialized itemId
		 *  And: initialized userSpace & userEmail
		 * 	And: initialized timeStamp
		 * 	And: our space 
		 * */
		
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		ItemBoundary actualItem = restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, 
				ItemBoundary.class, this.space, this.userEmail);
		
		assertThat(actualItem).isNotNull();
		assertThat(actualItem.getItemId()).isNotNull();
		assertThat(actualItem.getItemId().getSpace()).isEqualTo(this.space);
		assertThat(actualItem.getItemId().getId()).isNotNull();
		assertThat(actualItem.getCreatedTimestamp()).isNotNull();
		assertThat(actualItem.getType()).isEqualTo(itemType);
		assertThat(actualItem.getName()).isEqualTo(itemName);
		assertThat(actualItem.getCreatedBy()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(actualItem.getCreatedBy().getUserId().getSpace()).isEqualTo(this.space);
	}
	
	@Test
	public void testUpdateItem() throws Exception {
		/* Given: the server is up 
		 * And: the item is exist
		 * When: we PUT to /items/{userSpace}/{userEmail}/{itemSpace}/{itemId}
		 * Then: the server will update an existing item
		 * And: returns a msg with the updated item
		 * */
		String updatedType = "anotherDemoType";
		String updatedName = "anotherDemoItem";
		
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		ItemBoundary actualItem = restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, 
				ItemBoundary.class, this.space, this.userEmail);
		
		//PUT
//		String itemId = actualItem.getItemId().getId().split("_")[1];
		String itemId = actualItem.getItemId().getId();
		
		ItemBoundary updatedItem = new ItemBoundary(updatedType, updatedName, false);
		this.restTemplate.put(baseUrl + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", updatedItem,
				this.space, this.userEmail, this.space, itemId);
		
		//GET
		actualItem = restTemplate.getForObject(baseUrl + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", ItemBoundary.class,
				this.space, this.userEmail, this.space, itemId);
		assertThat(actualItem).isNotNull();
		assertThat(actualItem.getItemId()).isNotNull();
		assertThat(actualItem.getItemId().getSpace()).isEqualTo(this.space);
		assertThat(actualItem.getItemId().getId()).isNotNull();
		assertThat(actualItem.getCreatedTimestamp()).isNotNull();
		assertThat(actualItem.getType()).isEqualTo(updatedType);
		assertThat(actualItem.getName()).isEqualTo(updatedName);
		assertThat(actualItem.getCreatedBy()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(actualItem.getCreatedBy().getUserId().getSpace()).isEqualTo(this.space);
		assertThat(actualItem.getActive()).isEqualTo(false);
	}
	
	@Test
	public void testGetSpecificItem() throws Exception {
		/* Given: the server is up
		 * And: there are items in the DB
		 * And: we look for an existing item
		 * When: we GET to /items/{userSpace}/{userEmail}/{itemSpace}/{itemId}
		 * Then: the server will return that item as an ItemBoundary
		 */
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		ItemBoundary actualItem = restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, 
				ItemBoundary.class, this.space, this.userEmail);
		
		String itemId = actualItem.getItemId().getId();
		
		//GET
		actualItem = restTemplate.getForObject(baseUrl + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", ItemBoundary.class,
				this.space, this.userEmail, this.space, itemId);
		assertThat(actualItem).isNotNull();
		assertThat(actualItem.getItemId()).isNotNull();
		assertThat(actualItem.getItemId().getSpace()).isEqualTo(this.space);
		assertThat(actualItem.getItemId().getId()).isNotNull();
		assertThat(actualItem.getCreatedTimestamp()).isNotNull();
		assertThat(actualItem.getType()).isEqualTo(itemType);
		assertThat(actualItem.getName()).isEqualTo(itemName);
		assertThat(actualItem.getCreatedBy()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(actualItem.getCreatedBy().getUserId().getSpace()).isEqualTo(this.space);
	}
	
	@Test
	public void testGetAllItems() throws Exception {
		/* Given: the server is up
		 * When: we GET to items/{userSpace}/{userEmail}
		 * Then: the server will returns all the items stored in the DB 
		 */
		String anotherItemType = "anotherDemoType";
		String anotherItemName = "anotherItemName";
		
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		this.restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, ItemBoundary.class, 
				this.space, this.userEmail);
		
		item = new ItemBoundary(anotherItemType, anotherItemName, false, new LocationBoundary(1, 1));
		this.restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, ItemBoundary.class, 
				this.space, this.userEmail);
		
		ItemBoundary[] items = this.restTemplate.getForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", ItemBoundary[].class,
				this.space, this.userEmail);
		
		assertThat(items).isNotNull();
		assertThat(items).hasSize(2);
		
	}
	
	@Test
	public void testDeleteAllItems() throws Exception {
		/* Given: the server is up
		 * And: there're Items in the DB
		 * When: we DELETE to /admin/items/{userSpace}/{userEmail}
		 * Then: the server will delete all the items in the DB
		 */
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		this.restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, ItemBoundary.class, 
				this.space, this.userEmail);
		
		this.restTemplate.delete(baseUrl + "/admin/items/{userSpace}/{userEmail}", this.space, this.userEmail);
	}
}
