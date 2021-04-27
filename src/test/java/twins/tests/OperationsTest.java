package twins.tests;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.item.ItemIdBoundary;
import twins.operations.ItemWrapper;
import twins.operations.OperationBoundary;
import twins.operations.UserIdWrapper;
import twins.user.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OperationsTest {
	private int port;
	private String baseUrtl;
	private String space;
	private RestTemplate restTemplate;
	
	//dummy test variables
	private String userEmail = "a@demo.com";
	
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void initTest() {
		this.baseUrtl = "http://localhost:" + this.port + "/twins";
		this.space = "2021b.iftach.avraham";
		this.restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void tearDown() {
		this.restTemplate.delete(this.baseUrtl + "/admin/operations/{userSpace}/{userEmail}", this.space, this.userEmail);
	}
	
	@Test
	public void testContext() throws Exception { }
	
	@Test
	public void testInvokeOperation() throws Exception {
		/*
		 * Given: the server is up
		 * When: we POST an operation without operationId to /twins/operations
		 * Then: the server will create an id to the operation
		 * And: will set the space to our space
		 * And: will update the time the operation was activated
		 * And: will store that operation in the DB
		 * And: will return any JSON object
		 */
		
		//init demo operation
		String operationType = "demoOperation";
		ItemIdBoundary itemId = new ItemIdBoundary("99", this.space);
		ItemWrapper itemWrapper = new ItemWrapper();
		itemWrapper.setItemId(itemId);
		
		UserId userId = new UserId(this.space, this.userEmail);
		UserIdWrapper userWrapper = new UserIdWrapper();
		userWrapper.setUserId(userId);
		
		OperationBoundary operation = new OperationBoundary()
	}
	
	@Test @Disabled
	public void testInvokeASyncperation() throws Exception {
		/*
		 * Given: the server is up
		 * When: we POST an operation without operationId to /twins/operations/async
		 * Then: the server will create an id to the operation
		 * And: will set the space to our space
		 * And: will update the time the operation was activated
		 * And: will store that operation in the DB
		 * And: will return an OperationBoundary with all the updated details
		 */
	}
	
	@Test
	public void testExportAllOperations() throws Exception {
		/*
		 * Given: the server is up
		 * And: there's at least one operation in the DB
		 * When: we GET to /twins/admin/operations/{userSpace}/{userEmail}
		 * Then: the server will return all the operations stored in the DB 
		 */
	}
	
	@Test
	public void testDeleteAllOperations() throws Exception {
		/*
		 * Given: the server is up
		 * And: there's at least one operation in the DB
		 * When: we DELETE to /twins/admin/operations/{userSpace}/{userEmail}
		 * Then: the server will delete all the operations stored in the DB
		 */
	}
}
