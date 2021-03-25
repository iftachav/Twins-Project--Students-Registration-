package twins.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.logic.UsersService;

@RestController
public class UserController {
	private UsersService userService;
	
	@Autowired
	public UserController(UsersService userService) {
		this.userService=userService;
	}

		@RequestMapping(
				path = "/twins/users",
				method = RequestMethod.POST,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary createNewUser(@RequestBody NewUserDetails input) {
			UserBoundary rv= new UserBoundary();
			rv.setAvatar(input.getAvatar());
			rv.setRole(input.getRole());
			rv.getUserId().setEmail(input.getEmail());
			rv.setUsername(input.getUsername());
			return userService.createUser(rv);
		}
	
		@RequestMapping(
				path = "/twins/users/login/{userSpace}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary loginAndRetrieve(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
//			System.err.println("");
			return userService.login(userSpace, userEmail);
		}
		
		@RequestMapping(
				path = "/twins/users/{userSpace}/{userEmail}",
				method = RequestMethod.PUT,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public void updateUserDetails(@RequestBody UserBoundary input, @PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			userService.updateUser(userSpace, userEmail, input);
		}
		
		@RequestMapping(
				path = "/twins/admin/users/{userSpace}/{userEmail}",
				method = RequestMethod.DELETE)
		public void deleteAllUsers(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			userService.deleteAllUsers(userSpace, userEmail);
		}
		
		@RequestMapping(
				path = "/twins/admin/users/{userSpace}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String userSpace, @PathVariable("userEmail") String userEmail) {
			return userService.getAllUsers(userSpace, userEmail).toArray(new UserBoundary[0]);
		}
}
