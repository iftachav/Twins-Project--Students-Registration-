package twins.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import twins.user.NewUserDetails;
import twins.user.UserController;

class UserControllerTest {

	@Test
	void testUserController() {
		UserController us = new UserController(null);
		assertEquals(us.createNewUser(new NewUserDetails()), new NewUserDetails());
		
		fail("Not yet implemented");
	}

	@Test
	void testCreateNewUser() {
//		fail("Not yet implemented");
	}

	@Test
	void testLoginAndRetrieve() {
//		fail("Not yet implemented");
	}

	@Test
	void testUpdateUserDetails() {
//		fail("Not yet implemented");
	}

	@Test
	void testDeleteAllUsers() {
//		fail("Not yet implemented");
	}

	@Test
	void testExportAllUsers() {
//		fail("Not yet implemented");
	}

}
