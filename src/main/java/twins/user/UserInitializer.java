package twins.user;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import twins.data.UserEntity;
import twins.logic.UpdatedItemService;
import twins.logic.UpdatedOperationService;
import twins.logic.UpdatedUsersService;

@Component
public class UserInitializer implements CommandLineRunner{
	private UpdatedUsersService userService;
	private UpdatedItemService itemService;
	private UpdatedOperationService operationService;
	private String space;
	
	@Autowired
	public UserInitializer(UpdatedUsersService userService, UpdatedItemService itemService, UpdatedOperationService operationService) {
		this.userService = userService;
		this.itemService = itemService;
		this.operationService = operationService;
	}
	
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	public void run(String... args) throws Exception {
		char userNames[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		String email = "@demo.com";
		String role = "player".toUpperCase();
		
		IntStream.range(0, 20).mapToObj(
				i->{
					char userName = userNames[i];
					UserBoundary user = new UserBoundary(role, Character.toString(userName), Character.toString(userName));
					user.setUserId(new UserId(this.space, Character.toString(userName) + email));
					return user;
				}).forEach(obj->this.userService.createUser(obj));
		
		UserBoundary admin = new UserBoundary("ADMIN", "admin", "A");
		
		admin.setUserId(new UserId(this.space, "admin@demo.com"));		
		this.userService.createUser(admin);
//		List<UserBoundary> users = this.userService.getAllUsers(space, email, 0, 20);
//		for(UserBoundary user : users) {
//			
//		}
	}
}
