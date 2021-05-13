package twins.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twins.dal.ItemDao;
import twins.data.ItemEntity;
import twins.data.OperationEntity;
import twins.data.UserEntity;
import twins.item.ItemBoundary;
import twins.logic.ItemConverter;
import twins.logic.OperationEntityConverter;
import twins.logic.OperationHandler;
import twins.logic.UserItemConverter;

@Component
public class OperationHandlerImpl implements OperationHandler{

	private ItemDao itemDao;
	private ItemConverter itemConverter;
	private UserItemConverter userToItemConverter;
	private OperationEntityConverter operationEntityConverter;
	private String grade;
	private String id;

	public OperationHandlerImpl() { }

	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Autowired
	public void setItemConverter(ItemConverter itemConverter) {
		this.itemConverter = itemConverter;
	}

	@Autowired
	public void setOperationEntityConverter(OperationEntityConverter operationEntityConverter) {
		this.operationEntityConverter = operationEntityConverter;
	}

	@Autowired
	public void setUserToItemConverter(UserItemConverter userToItemConverter) {
		this.userToItemConverter = userToItemConverter;
	}
	
	@Value("${studentGrade:Grade}")
	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Value("${studentId:Id}")
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void registerToCourse(OperationEntity operation, ItemEntity item, UserEntity user) {
		//convert user to item
		//add the Student item to the Course item
		
		//register invoker
		if(operation.getOperationAttributes() == null || operation.getOperationAttributes().equals("")) {
			
			//Check whether the Student already in the Course participants list
			List<ItemEntity> students = item.getChildren()
											.stream()
											.filter(e-> {return this.operationEntityConverter
																	.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(user.getEmailAndSpace());}/*{return e.getName().equals(user.getEmailAndSpace());}*/)
											.collect(Collectors.toList());
			if(students.isEmpty())	 
				item.addChild(this.userToItemConverter.UserToItem(user));
			else
				item.getChildren()
					.stream()
					.filter(e-> {return this.operationEntityConverter
											.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(user.getEmailAndSpace());})
					.forEach(e-> e.setActive(true));
		}			
		else {	
			//register all users passed as OperationAttributes
			Map<String, Object> students = this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes());
			students.entrySet()
					.stream()
					.forEach(studentEntry->{ 
						//check whether each Student already in the Course participants list 
						List<ItemEntity> participants = item.getChildren()
															.stream()
															//.filter(e-> {return e.getName().equals(((UserEntity) studentEntry.getValue()).getEmailAndSpace().split("@@")[0]);})
															.filter(e-> {return this.operationEntityConverter
																	.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(((UserEntity) studentEntry.getValue()).getEmailAndSpace());})
															.collect(Collectors.toList());
					if(participants.isEmpty())
						item.addChild(this.userToItemConverter.UserToItem((UserEntity) studentEntry.getValue()));
					else
						item.getChildren()
							.stream()
							.filter(e->{return this.operationEntityConverter
									.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(((UserEntity) studentEntry.getValue()).getEmailAndSpace());})
							.forEach(e-> e.setActive(true));
				});
		}
		
	}

	@Override
	public void resignFromCourse(OperationEntity operation, ItemEntity item, UserEntity user) {
		//convert user to item
		//search parent item (Course) for students
		//remove Student item from parent 
		
		//remove invoker
		if(operation.getOperationAttributes() == null || operation.getOperationAttributes().equals(""))
			item.getChildren()
				.stream()
				.filter(e->{ return this.operationEntityConverter
						.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(user.getEmailAndSpace());})
				.forEach(e-> e.setActive(false));
		else {
			//remove all users passed as OperationAttributes
			Map<String, Object> users = operationEntityConverter.fromJsonToMap(operation.getOperationAttributes());
			users.entrySet()
				.stream()
				.forEach(userEntry->{
					item.getChildren()
					.stream()
					.filter(e-> {return this.operationEntityConverter
							.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(((UserEntity) userEntry.getValue()).getEmailAndSpace());})
					.forEach(e-> e.setActive(false));
				});
		}	
	}

	@Override
	public void updateGrade(OperationEntity operation, ItemEntity item) {
		//search for item Course
		//search for child item (Student)
		//update grade
		//we can assume there's only one grade passed
		Map<String, Object> grades = this.operationEntityConverter.fromJsonToMap(operation.getOperationAttributes());	
		String studentId = (String) grades.get(this.id);
		
		//iterate over all Courses' children (Student)
		//if child id == studentId:
		//update it's grade
		item.getChildren()
			.stream()
			.forEach(e-> {	
				if(this.operationEntityConverter.fromJsonToMap(e.getItemAttributes()).get(this.id).equals(studentId))
					this.itemConverter.fromMapToJson(
							(Map<String, Object>) this.itemConverter.fromJsonToMap(e.getItemAttributes()).put(this.grade, grades.get(this.grade)));
			});
		
		/*grades.entrySet()
			.stream()
			.forEach(gradesEntry->{	//iterate over each gradesEntry (gradesEntry: Map.Entry<String, int> ex. <"Grade", 80>)
				item.getChildren()
					.stream()
					//.filter(e-> {return e.getName().equals(studentEntry.getKey());})	//filter all children with name == student.email
					.filter(e-> {return this.operationEntityConverter
											 .fromJsonToMap(e.getItemAttributes()).get(this.id).equals(user.getEmailAndSpace());})
					.forEach(e-> e.setItemAttributes(	//set the grade of the student
						itemConverter.fromMapToJson(
								(Map<String, Object>) itemConverter.fromJsonToMap(e.getItemAttributes()).put(this.grade, gradesEntry.getValue()))));
			});*/
	}

	@Override
	public void removeCourse(OperationEntity operation, ItemEntity item) {
		//search for item
		//remove item
		List<ItemEntity> courses = this.itemDao.findAllById(operation.getItemId());
		courses.stream().forEach(e-> e.setActive(false));
	}

	@Override
	public List<ItemBoundary> getStudentsCourses(OperationEntity operation, UserEntity user) {
		Iterable<ItemEntity> items = this.itemDao.findAll();
		List<ItemEntity> courses = StreamSupport.stream(items.spliterator(), false).collect(Collectors.toList());
		
		List<ItemEntity> userCourses = new ArrayList<>();
		for (ItemEntity course : courses) {
			if(course.isActive()) {
				for(ItemEntity student : course.getChildren())
					if(student.isActive() && this.itemConverter
												.fromJsonToMap(student.getItemAttributes())
												.get(this.id).equals(user.getEmailAndSpace())/*student.getName().equals(user.getEmailAndSpace().split("@@")[0])*/)
						userCourses.add(course);
			}
		}
		return userCourses.stream().map(this.itemConverter::toBoundary).collect(Collectors.toList());
	}
	
}
