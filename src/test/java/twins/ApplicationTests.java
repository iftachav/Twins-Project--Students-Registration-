package twins;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import twins.user.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	private int port;
	private RestTemplate restTemplate;
	private String baseUrl;
	private String deleteUrl;
	private String springApplicationName;
	private String email1 = "iftach@gmail.com";
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init (){
		setSpringApplicationName(springApplicationName);
		deleteUrl = "http://localhost:" + this.port + "/twins/admin/users/" + this.springApplicationName + "/" + this.email1;
		this.restTemplate = new RestTemplate();
		this.baseUrl = "http://localhost:" + this.port + "/twins/users";
		
		
	}
	
	@BeforeEach
	public void setup() {
		// init operations before each test
		System.err.println("init before test..");
	}

	
	@AfterEach
	public void teardown () {
		this.restTemplate
			.delete("http://localhost:" + this.port + "/twins/admin/users/iftach/hello@gmail.com");
	}
	
	@Test
	public void testContext() throws Exception {
		// GIVEN the server is up
		
		// WHEN the application initializes
		
		// THEN spring starts up with no errors
	}
	
	@Test
	public void testCreateUser() {
		// GIVEN the server is up
		
		// WHEN I POST using /twins/users with {}
				
		// THEN spring starts up with no errors
//		NewUserDetails userDetails = new NewUserDetails();
		Map<String, Object> userDetails = new HashMap<>();
		UserBoundary actualUser = this.restTemplate
				.postForObject(baseUrl, userDetails , UserBoundary.class);
		UserBoundary expected = new UserBoundary();
		expected.getUserId().setSpace(springApplicationName);
		
		assertThat(actualUser)
			.isEqualTo(expected);
	}
	
	@Test //@Disabled
	public void testPostEmptyMessageOnServer() throws Exception {
		// GIVEN the server is up
		// do nothing
		
		// WHEN I POST using /hello with {}
		String theUrl = "http://localhost:" + this.port + "/hello";
		Map<String, Object> emptyMessage = new HashMap<>();
//		MessageBoundary actualMessage = this.restTemplate
//			.postForObject(theUrl, emptyMessage, MessageBoundary.class);
		this.restTemplate
		.getForObject(baseUrl+"/login/iftach/iftach@gmail.com", UserBoundary.class);
//			
		
		// THEN the server creates a default value and stores it in the database
		
//		if (actualMessage.getId() == null) {
//			throw new Exception("no id was generated for new message");
//		}
		// assert that the actual message is not null
//		assertThat(actualMessage)
//			.isNotNull();
//		
//		
//		// assert that the id is not null
//		// AND returns message with initialized unique id
//		assertThat(actualMessage.getId())
//			.isNotNull();
//
//		// AND initialized non null time-stamp
//		assertThat(actualMessage.getMessageTimestamp())
//			.isNotNull();
//		
//		// AND initialized not null version History (e.g. []) 
//		assertThat(actualMessage.getVersionHistory())
//			.overridingErrorMessage("expected version history not to be null")
//			.isNotNull();
//		
//		// AND version History array is of size 0
//		assertThat(actualMessage.getVersionHistory())
//			.hasSize(0);
	}
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}
}
