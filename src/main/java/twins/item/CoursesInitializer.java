package twins.item;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import twins.logic.UpdatedItemService;
import twins.logic.UpdatedUsersService;
import twins.user.UserBoundary;
import twins.user.UserId;

@Component
public class CoursesInitializer implements CommandLineRunner{
	private UpdatedItemService itemService;
	private UpdatedUsersService userService;
	private String space;
	
	@Autowired
	public CoursesInitializer(UpdatedItemService itemService, UpdatedUsersService userService) { 
		this.itemService = itemService;
		this.userService = userService;
	}
	
	@Value("${spring.application.name:2021b.iftach.avraham}")
	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	public void run(String... args) throws Exception {
		String email = "user@demo.com";
		UserBoundary user = new UserBoundary("MANAGER", "demo", "demo");
		user.setUserId(new UserId(this.space, email));
		userService.createUser(user);
		IntStream.range(0, 20).mapToObj(
				i->{
					ItemBoundary course = new ItemBoundary();
					course.setName(Integer.toString(1010 + i));
					course.setActive(true);
					course.setType("Course");
					return course;
				}).forEach(obj -> this.itemService.createItem(this.space, email, obj));
	}

}
