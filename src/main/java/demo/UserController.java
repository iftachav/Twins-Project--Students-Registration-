package demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

		@RequestMapping(
				path = "/twins/users",
				method = RequestMethod.POST,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary createNewUser(@RequestBody NewUserDetails input) {
			// STUB implementation - do nothing - focus on input and output only
			System.err.println("(STUB) successfully createNewUser");
			return new UserBoundary();
		}
	
		@RequestMapping(
				path = "/twins/users/login/{userSpace}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary loginAndRetrieve(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			// STUB implementation - do nothing - focus on input and output only
			System.err.println("(STUB) successfully loginAndRetrieve");
			return new UserBoundary();
		}
		
		@RequestMapping(
				path = "/twins/users/{userSpace}/{userEmail}",
				method = RequestMethod.PUT,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void updateUserDetails(@RequestBody UserBoundary input, @PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			// STUB implementation - do nothing - focus on input and output only
			System.err.println("(STUB) successfully updateUserDetails");
		}
		
		@RequestMapping(
				path = "/twins/admin/users/{userSpace}/{userEmail}",
				method = RequestMethod.DELETE)
		public void deleteAllUsers(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			// STUB implementation - do nothing
			System.err.println("(STUB) successfully deleteAllUsers");
		}
		
		@RequestMapping(
				path = "/twins/admin/users/{userSpace}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			// STUB implementation - do nothing - focus on input and output only
			System.err.println("(STUB) successfully exportAllUsers");
			AtomicLong counter = new AtomicLong(1L);
			// STUB implementation - create 3 messages as array and return it
			UserBoundary[] tmp = new UserBoundary[] {new UserBoundary(), new UserBoundary(), new UserBoundary()};
			tmp[0].setUsername("Moshiko");
			tmp[1].setRole("PLAYER");
			return tmp;
//			return 
//			  Stream
//				.of(new UserBoundary(),
//					new UserBoundary(),
//					new UserBoundary()) // create 3 boundaries
//				// convert each boundary to other type / object
//				.collect(Collectors.toList())// stream all boundaries to a collection;
//				.toArray(new UserBoundary[0]); // convert list to array
		}
}
