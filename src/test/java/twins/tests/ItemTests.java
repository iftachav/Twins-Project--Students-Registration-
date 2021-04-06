package twins.tests;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.item.ItemBoundary;
import twins.user.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemTests {
	private int port;
	private String space;
	private String baseUrl;
	private RestTemplate restTemplate;
	
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
		
		String url = this.baseUrl + "/items";
		UserId userId = new UserId();
		ItemBoundary item = new ItemBoundary();
		ItemBoundary actualItem = restTemplate.postForObject(url + "/{userSpace}/{userEmail}", item, ItemBoundary.class, this.space, "x@x.com");
		
		assertThat(actualItem).isNotNull();
		assertThat(actualItem.getItemId()).isNotNull();
		assertThat(actualItem.getItemId().getSpace()).isEqualTo(space);
	}
	
	@Test
	public void testUpdateItem() {
		/* Given: the server is up 
		 * And: the item is exist
		 * When: we PUT to /items{userSpace}/{userEmail}/{itemSpace}/{itemId}
		 * Then: the server will update an existing item
		 * And: returns a msg with the updated item
		 * */
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
