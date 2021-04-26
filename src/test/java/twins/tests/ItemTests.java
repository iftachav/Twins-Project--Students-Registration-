package twins.tests;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.item.ItemBoundary;
import twins.item.LocationBoundary;
import twins.user.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemTests {
	private int port;
	private String space;
	private String baseUrl;
	private RestTemplate restTemplate;
	
	//dummy items variables
	private String userEmail = "a@demo.com";
	private String itemName = "demoItem";
	private String itemType = "demoType";
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void initTest() {
		this.restTemplate = new RestTemplate();
		this.baseUrl = "localhost:" + this.port + "/twins";
		this.space = "2021b.iftach.avraham";
	}
	
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
		ItemBoundary actualItem = restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, ItemBoundary.class, this.space, this.userEmail);
		
		assertThat(actualItem).isNotNull();
		assertThat(actualItem.getItemId()).isNotNull();
		assertThat(actualItem.getItemId().getSpace()).isEqualTo(this.space);
		assertThat(actualItem.getItemId().getId()).isNotNull();
		assertThat(actualItem.getCreatedTimestamp()).isNotNull();
		assertThat(actualItem.getType()).isEqualTo(itemType);
		assertThat(actualItem.getName()).isEqualTo(itemName);
		assertThat(actualItem.getCreatedTimestamp()).isNotNull();
		assertThat(actualItem.getCreatedBy()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId()).isNotNull();
		assertThat(actualItem.getCreatedBy().getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(actualItem.getCreatedBy().getUserId().getSpace()).isEqualTo(this.space);
	}
	
	@Test
	public void testUpdateItem() {
		/* Given: the server is up 
		 * And: the item is exist
		 * When: we PUT to /items/{userSpace}/{userEmail}/{itemSpace}/{itemId}
		 * Then: the server will update an existing item
		 * And: returns a msg with the updated item
		 * */
		String updatedType = "anotherDemoType";
		String updatedName = "anotherDemoItem";
		
		ItemBoundary item = new ItemBoundary(itemType, itemName, true, new LocationBoundary(1, 1));
		ItemBoundary actualItem = restTemplate.postForObject(this.baseUrl + "/items/{userSpace}/{userEmail}", item, ItemBoundary.class, this.space, this.userEmail);
		
		//PUT
		String itemId = actualItem.getItemId().getId();
		ItemBoundary updatedItem = new ItemBoundary(updatedType, updatedName, false);
		this.restTemplate.put(baseUrl + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", updatedItem,
				this.space, this.userEmail, this.space, itemId);
	}
	
	@Test
	public void testGetSpecificItem() {
		/* Given: the server is up
		 * And: there are items in the DB
		 * And: we look for an existing item
		 * When: we GET to /items/{userSpace}/{userEmail}/{itemSpace}/{itemId}
		 * Then: the server will return that item as an ItemBoundary
		 */
	}
	
	@Test
	public void testGetAllItems() {
		/* Given: the server is up
		 * When: we GET to items/{userSpace}/{userEmail}
		 * Then: the server will returns all the items stored in the DB 
		 */
	}
	
	@Test
	public void testDeleteAllItems() {
		/* Given: the server is up
		 * And: there're Items in the DB
		 * When: we DELETE to /admin/items/{userSpace}/{userEmail}
		 * Then: the server will delete all the items in the DB
		 */
	}
}
