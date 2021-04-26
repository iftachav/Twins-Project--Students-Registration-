package twins.tests;


import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTest {

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
		this.baseUrl = "http://localhost:" + this.port + "/twins";
		this.space = "2021b.iftach.avraham";
	}
	
	@Test
	public void testContext() throws Exception { }

	@Test
	void testCreateNewUser() {
		/* Given: the server is up
		 * When: we POST a valid NewUserDetails JSON to /twins/users
		 * Then: the server will create a new user with the provided details and will store it in the DB
		 * And: returns an UserBoundary with the provided details and initialized UserId 
		 */
		this.baseUrl += "/users";
	}

	@Test
	void testLoginAndRetrieve() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: We send a GET request to /twins/users/login/{userSpace}/{userEmail} with the correct userSpace and existing userEmail
		 * Then: the server will return a UserBoundary with the correct user details
		 */
	}

	@Test
	void testUpdateUserDetails() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: we send a PUT request to /twins/users/{userSpace}/{userEmail} with a valid user details containing a JSON with valid data
		 * Then: The server will update the requested user with the data provided
		 */
	}

	@Test
	void testDeleteAllUsers() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: we send a DELETE request to /twins/admin/users/{userSpace}/{userEmail} 
		 * Then: The server will delete all the users in the DB
		 */
	}

	@Test
	void testExportAllUsers() {
		/* Given: The server is up
		 * When: we send a GET request to /twins/admin/users/{userSpace}/{userEmail} 
		 * Then: The server will returns all the Users in the DB as array of UserBoundary
		 */
	}

}
