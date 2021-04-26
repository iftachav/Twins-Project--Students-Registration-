package twins.tests;


import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import twins.user.NewUserDetails;
import twins.user.UserBoundary;

/*
 * TODO: add tests for the following edge cases:
 * 1. create a user with invalid mail/role/space/null values
 * 2. create a user with the same mail as an existing user but with different details
 * 3. update space/mail/timeStamp of an existing user
 * 4. update a user with null/invalid values
 * How can we test there's nothing on the server after a DELETE?
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTest {

	private int port;
	private String space;
	private String baseUrl;
	private RestTemplate restTemplate;
	
	//dummy User variables
	private String userEmail;
	private String userRole;
	private String username;
	private String userAvatar;
	
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
		this.userRole = "PLAYER";
		this.username = "user A";
		this.userAvatar = "A";
	}
	
	@AfterEach
	public void tearDown() {
		this.restTemplate.delete(this.baseUrl + "/admin/users/{userSpace}/{userEmail}", this.space, this.userEmail);
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
		String url = this.baseUrl + "/users";
		NewUserDetails newUser = new NewUserDetails(userEmail, userRole, username, userAvatar);
		UserBoundary userBoundary = this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		assertThat(userBoundary).isNotNull();
		assertThat(userBoundary.getUserId()).isNotNull();
		assertThat(userBoundary.getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(userBoundary.getUserId().getSpace()).isEqualTo(this.space);
	}


	@Test
	void testLoginAndRetrieve() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: We send a GET request to /twins/users/login/{userSpace}/{userEmail} with the correct userSpace and existing userEmail
		 * Then: the server will return a UserBoundary with the correct user details
		 */
	
		String url = this.baseUrl + "/users";
		NewUserDetails newUser = new NewUserDetails(userEmail, userRole, username, userAvatar);
		this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		
		url = this.baseUrl + "/users/login/{userSpace}/{userEmail}";
		UserBoundary returnedUser = this.restTemplate.getForObject(url, UserBoundary.class, this.space, this.userEmail);
		assertThat(returnedUser).isNotNull();
		assertThat(returnedUser.getUserId()).isNotNull();
		assertThat(returnedUser.getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(returnedUser.getUserId().getSpace()).isEqualTo(this.space);
		assertThat(returnedUser.getRole()).isEqualTo(this.userRole);
		assertThat(returnedUser.getUsername()).isEqualTo(this.username);
		assertThat(returnedUser.getAvatar()).isEqualTo(this.userAvatar);
	}

	@Test
	void testUpdateUserDetails() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: we send a PUT request to /twins/users/{userSpace}/{userEmail} with a valid user details containing a JSON with valid data
		 * Then: The server will update the requested user with the data provided
		 */
		String changedUsername = "User B";
		String changedUserAvatar = "B";
		String changedUserRole = "MANAGER";
		
		String url = this.baseUrl + "/users";
		NewUserDetails newUser = new NewUserDetails(userEmail, userRole, username, userAvatar);
		this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		
		//PUT changes
		url = this.baseUrl + "/users/{userSpace}/{userEmail}";
		UserBoundary updateUser = new UserBoundary(changedUserRole, changedUsername, changedUserAvatar);
		this.restTemplate.put(url, updateUser, this.space, this.userEmail);
		
		//GET changed User
		url = this.baseUrl + "/users/login/{userSpace}/{userEmail}";
		UserBoundary returnedUser = this.restTemplate.getForObject(url, UserBoundary.class, this.space, this.userEmail);
		assertThat(returnedUser).isNotNull();
		assertThat(returnedUser.getUserId()).isNotNull();
		assertThat(returnedUser.getUserId().getEmail()).isEqualTo(this.userEmail);
		assertThat(returnedUser.getUserId().getSpace()).isEqualTo(this.space);
		assertThat(returnedUser.getRole()).isEqualTo(changedUserRole);
		assertThat(returnedUser.getUsername()).isEqualTo(changedUsername);
		assertThat(returnedUser.getAvatar()).isEqualTo(changedUserAvatar);
	}

	@Test
	void testExportAllUsers() {
		/* Given: The server is up
		 * When: we send a GET request to /twins/admin/users/{userSpace}/{userEmail} 
		 * Then: The server will returns all the Users in the DB as array of UserBoundary
		 */
		
		String secondUserEmail = "b@demo.com";
		String secondUserRole = "PLAYER";
		String secondUsername = "user B";
		String secondUserAvatar = "B";
		
		String url = this.baseUrl + "/users";
		NewUserDetails  newUser = new NewUserDetails(this.userEmail, this.userRole, this.username, this.userRole);
		this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		newUser = new NewUserDetails(secondUserEmail, secondUserRole, secondUsername, secondUserAvatar);
		this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		
		url = this.baseUrl + "/admin/users/{userSpace}/{userEmail}";
		UserBoundary[] users = this.restTemplate.getForObject(url, UserBoundary[].class, this.space, this.userEmail);
		assertThat(users).overridingErrorMessage("There are no users in the DB").isNotNull();
		assertThat(users).hasSize(2);
	}
	
	@Test
	void testDeleteAllUsers() {
		/* Given: The server is up
		 * And: There is at least 1 user in the DB
		 * When: we send a DELETE request to /twins/admin/users/{userSpace}/{userEmail} 
		 * Then: The server will delete all the users in the DB
		 */
		String url = this.baseUrl + "/users";
		NewUserDetails newUser = new NewUserDetails(userEmail, userRole, username, userAvatar);
		this.restTemplate.postForObject(url, newUser, UserBoundary.class);
		
		url = this.baseUrl + "/admin/users/{userSpace}/{userEmail}";
		this.restTemplate.delete(url, this.space, this.userEmail);
	}

}
